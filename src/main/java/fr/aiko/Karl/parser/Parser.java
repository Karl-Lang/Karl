package fr.aiko.Karl.parser;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.errors.SyntaxError.SemiColonError;
import fr.aiko.Karl.errors.SyntaxError.SyntaxError;
import fr.aiko.Karl.parser.ast.expressions.*;
import fr.aiko.Karl.parser.ast.statements.*;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.LogicalOperators;
import fr.aiko.Karl.std.Operators;
import fr.aiko.Karl.std.Types;
import fr.aiko.Karl.std.VariableManager;

import java.util.ArrayList;
import java.util.Arrays;

public final class Parser {
    public final String fileName;
    private final int size;
    private final ArrayList<Token> tokens;
    private final ArrayList<Statement> statements = new ArrayList<>();
    private int pos;

    public Parser(ArrayList<Token> tokens, String fileName) {
        this.tokens = tokens;
        this.fileName = fileName;
        this.pos = 0;
        this.size = tokens.size();
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
            return variableDeclaration();
        } else if (checkType(0, TokenType.IDENTIFIER) && checkType(1, TokenType.EQUAL)) {
            return variableAssignment();
        } else if (match(TokenType.IF)) {
            return ifElse();
        } else {
            new RuntimeError("Unknown statement : " + get(0).getValue(), fileName, get(0).getLine());
            return null;
        }
    }

    private Expression getExpression() {
        Token token = get(0);
        Expression expr = null;
        if (match(TokenType.STRING) || match(TokenType.INT) || match(TokenType.BOOL) || match(TokenType.FLOAT) || match(TokenType.CHAR)) {
            switch (token.getType()) {
                case STRING -> expr = new ValueExpression(token.getValue(), token.getType());
                case INT -> expr = new ValueExpression(Integer.parseInt(token.getValue()), token.getType());
                case BOOL -> expr = new ValueExpression(Boolean.parseBoolean(token.getValue()), token.getType());
                case FLOAT -> expr = new ValueExpression(Float.parseFloat(token.getValue()), token.getType());
                case CHAR -> expr = new ValueExpression(token.getValue().charAt(0), token.getType());
            }
        } else if (match(TokenType.IDENTIFIER)) {
            expr = new VariableCallExpression(token.getValue());
        } else if (match(TokenType.LEFT_PARENTHESIS)) {
            ConditionalExpression expression = getConditionalExpression();
            skip(TokenType.RIGHT_PARENTHESIS);
            return expression;
        } else return getConditionalExpression();

        if (Operators.isOperator(get(0).getType())) {
            return getOperationExpression(expr);
        } else return expr;
    }

    private BlockStatement getBlock() {
        ArrayList<Statement> statements = new ArrayList<>();
        skip(TokenType.MINUS);
        skip(TokenType.GREATER);
        skip(TokenType.LEFT_BRACE);
        while (!checkType(0, TokenType.RIGHT_BRACE) && !checkType(0, TokenType.EOF) && pos < size - 1) {
            Statement statement = getStatement();
            if (statement != null) {
                statements.add(statement);
            }
        }
        skip(TokenType.RIGHT_BRACE);
        return new BlockStatement(statements);
    }

    private ConditionalExpression getConditionalExpression() {
        ConditionalExpression result;
        Expression left = getExpression();
        if (LogicalOperators.isOperator(get(0).getType())) {
            Token operator = get(0);
            skip(operator.getType());
            Expression right = getExpression();
            result = new ConditionalExpression(operator.getType(), left, right);
        } else result = new ConditionalExpression(null, left, null);

        if (LogicalOperators.isOperator(getType())) {
            TokenType type = getType();
            match(type);
            return new ConditionalExpression(type, result, getConditionalExpression());
        } else return result;
    }

    private OperationExpression getOperationExpression(Expression left) {
        if (left == null) {
            new RuntimeError("Unknown expression : " + get(0).getValue(), fileName, get(0).getLine());
            return null;
        }
        Token operator = get(0);
        if (!Operators.isOperator(operator.getType())) {
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

    private Statement ifElse() {
        Expression condition = getExpression();
        BlockStatement ifBlock = getBlock();
        if (match(TokenType.ELSE)) {
            BlockStatement elseBlock = getBlock();
            return new IfElseStatement(condition, ifBlock, elseBlock);
        } else return new IfElseStatement(condition, ifBlock, null);
    }

    private Statement variableAssignment() {
        String name = get(0).getValue();
        match(TokenType.IDENTIFIER);
        skip(TokenType.EQUAL);
        Expression expr = getExpression();
        if (expr == null) {
            new RuntimeError("Unknown expression : " + get(0).getValue(), fileName, get(0).getLine());
            return null;
        }

        Value var = VariableManager.getVariable(name);
        if (var == null) {
            new RuntimeError("Variable " + name + " is not declared", fileName, get(0).getLine());
            return null;
        }
        if (var.getType() != expr.eval().getType()) {
            new RuntimeError("Type mismatch : " + var + " and " + expr.eval(), fileName, get(0).getLine());
            return null;
        }
        skip(TokenType.SEMICOLON);

        return new VariableAssignmentStatement(name, expr.eval());
    }

    private Statement variableDeclaration() {
        Token type = get(0);
        match(type.getType());
        skip(TokenType.COLON);
        Token name = get(0);
        skip(TokenType.IDENTIFIER);
        skip(TokenType.EQUAL);
        Expression expression = getExpression();
        if (expression == null) {
            new RuntimeError("Excepted expression after " + name.getValue(), fileName, name.getLine());
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
                new RuntimeError("Unknown expression: " + get(0).getValue(), fileName, get(0).getLine());
            }
            expressions.add(expr);
            if (!match(TokenType.COMMA) && !checkType(0, TokenType.RIGHT_PARENTHESIS) && !checkType(1, TokenType.RIGHT_PARENTHESIS)) {
                new SyntaxError("Excepted ',' for separate parameters", fileName, get(0).getLine());
            }
        }
        if (!match(TokenType.SEMICOLON)) new SemiColonError(fileName, get(0).getLine());
        return new ShowStatement(expressions);
    }

    private void skip(TokenType type) {
        if (get(0).getType() != type) {
            new RuntimeError("Excepted " + type.toString().toLowerCase() + " but got " + get(0).getType().toString().toLowerCase(), fileName, get(0).getLine());
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

    private TokenType getType() {
        return get(0).getType();
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
