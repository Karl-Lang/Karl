package fr.aiko.Karl.parser;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.errors.SyntaxError.SemiColonError;
import fr.aiko.Karl.errors.SyntaxError.SyntaxError;
import fr.aiko.Karl.parser.ast.expressions.Expression;
import fr.aiko.Karl.parser.ast.expressions.OperationExpression;
import fr.aiko.Karl.parser.ast.expressions.ValueExpression;
import fr.aiko.Karl.parser.ast.expressions.VariableExpression;
import fr.aiko.Karl.parser.ast.statements.ShowStatement;
import fr.aiko.Karl.parser.ast.statements.Statement;
import fr.aiko.Karl.parser.ast.statements.VariableDeclarationStatement;
import fr.aiko.Karl.std.Operators;
import fr.aiko.Karl.std.Types;
import fr.aiko.Karl.std.VariableManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public final class Parser {
    private int pos;
    private final int size;
    private final ArrayList<Token> tokens;
    public final String fileName;
    private final ArrayList<Statement> statements = new ArrayList<>();
    private final HashMap<String, TokenType> OPERATORS = new HashMap<>();

    public Parser(ArrayList<Token> tokens, String fileName) {
        this.tokens = tokens;
        this.fileName = fileName;
        this.pos = 0;
        this.size = tokens.size();

        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.MULTIPLY);
        OPERATORS.put("/", TokenType.DIVIDE);
        OPERATORS.put("=", TokenType.EQUAL);
        OPERATORS.put("==", TokenType.EQUALEQUAL);
        OPERATORS.put(">", TokenType.GREATER);
        OPERATORS.put("<", TokenType.LESS);
    }

    public ArrayList<Statement> parse() {
        while (pos < size - 1 && !checkType(0, TokenType.EOF)) {
            Statement statement = getStatement();
            if (statement != null) {
                statements.add(statement);
            }
        }
        return statements;
    }

    private Statement getStatement() {
        if (match(TokenType.SHOW)) {
            return show();
        } else if (Types.contains(get(0).getType())) {
            return varDeclaration();
        } else {
            new RuntimeError("Unknown statement : " + get(0).getValue(), fileName, get(0).getLine());
            return null;
        }
    }

    private Expression getExpression() {
        Token token = get(0);
        Expression expr = null;
        if (match(TokenType.STRING) || match(TokenType.INT) || match(TokenType.BOOL) || match(TokenType.FLOAT)) {
            switch (token.getType()) {
                case STRING -> expr = new ValueExpression(token.getValue(), token.getType());
                case INT -> expr = new ValueExpression(Integer.parseInt(token.getValue()), token.getType());
                case BOOL -> expr = new ValueExpression(Boolean.parseBoolean(token.getValue()), token.getType());
                case FLOAT -> expr = new ValueExpression(Float.parseFloat(token.getValue()), token.getType());
            }
        } else if (match(TokenType.IDENTIFIER)) {
            expr = new ValueExpression(VariableManager.getVariable(token.getValue()), token.getType());
        }

        if (Operators.isType(get(0).getType())) {
            return getOperationExpression(expr);
        } else return expr;
    }

    private OperationExpression getOperationExpression(Expression left) {
        if (left == null) {
            new RuntimeError("Unknown expression : " + get(0).getValue(), fileName, get(0).getLine());
            return null;
        }
        Token operator = get(0);
        if (!Operators.isType(operator.getType())) {
            new RuntimeError("Unknown operator : " + operator.getValue(), fileName, operator.getLine());
            return null;
        }
        match(operator.getType());
        Expression right = getExpression();
        if (right == null) {
            new RuntimeError("Unknown expression : " + get(0).getValue(), fileName, get(0).getLine());
            return null;
        }

        if (right.eval().getType() != left.eval().getType() && (!Arrays.asList(new TokenType[]{TokenType.INT, TokenType.FLOAT}).contains(right.eval().getType()) || !Arrays.asList(new TokenType[]{TokenType.INT, TokenType.FLOAT}).contains(left.eval().getType()))) {
            new RuntimeError("Type mismatch : " + left.eval() + " and " + right.eval(), fileName, get(0).getLine());
            return null;
        }

        return new OperationExpression(left, right, operator.getType());
    }

    private Statement varDeclaration() {
        Token type = get(0);
        match(type.getType());
        skip(TokenType.COLON);
        Token name = get(0);
        skip(TokenType.IDENTIFIER);
        skip(TokenType.EQUAL);
        Expression expression = getExpression();
        if (expression == null) {
            new RuntimeError("Expected expression after " + name.getValue(), fileName, name.getLine());
            return null;
        }

        if (expression.eval().getType() != type.getType()) {
            new RuntimeError("Excepted type " + type.getValue() + " but got " + expression.eval().getType().toString().toLowerCase(), fileName, get(0).getLine());
            return null;
        }

        if (!match(TokenType.SEMICOLON)) {
            new SemiColonError(fileName, get(0).getLine());
            return null;
        }
        return new VariableDeclarationStatement(new VariableExpression(name.getValue(), expression.eval()));
    }

    private ShowStatement show() {
        skip(TokenType.LEFT_PARENTHESIS);
        ArrayList<Expression> expressions = new ArrayList<>();
        while (!match(TokenType.RIGHT_PARENTHESIS)) {
            Expression expr = getExpression();
            if (expr == null) {
                new RuntimeError("Unknow expression: " + get(0).getValue(), fileName, get(0).getLine());
            }
            expressions.add(expr);
            if (!match(TokenType.COMMA) && !checkType(0, TokenType.RIGHT_PARENTHESIS) && !checkType(1, TokenType.RIGHT_PARENTHESIS)) {
                new SyntaxError("Expected ',' for separate parameters", fileName, get(0).getLine());
            }
        }
        if (!match(TokenType.SEMICOLON)) new SemiColonError(fileName, get(0).getLine());
        return new ShowStatement(expressions);
    }

    private void skip(TokenType type) {
        if (get(0).getType() != type) {
            new RuntimeError("Expected " + type + " but got " + get(0).getType(), fileName, get(0).getLine());
        }
        pos++;
    }

    private Token get(int relativePosition) {
        int newPos = pos + relativePosition;
        if (newPos >= size || newPos < 0) {
            return new Token(TokenType.EOF, "", -1, -1);
        }
        return tokens.get(pos + relativePosition);
    }

    private boolean checkType(int pos, TokenType type) {
        return get(pos).getType() == type;
    }

    private boolean match(TokenType type) {
        if (checkType(0, type)) {
            pos++;
            return true;
        }
        return false;
    }
}
