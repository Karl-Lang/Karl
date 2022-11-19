package fr.aiko.Karl.parser;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.errors.SyntaxError.SemiColonError;
import fr.aiko.Karl.errors.SyntaxError.SyntaxError;
import fr.aiko.Karl.parser.ast.expressions.*;
import fr.aiko.Karl.parser.ast.statements.*;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
            if (match(TokenType.COMMENTARY)) {
                int baseLine = get(0).getLine();
                while (get(0).getLine() == baseLine && pos < size - 1 && !checkType(0, TokenType.EOF)) {
                    match(getType());
                }
                if (pos == size - 1 || checkType(0, TokenType.EOF)) break;
            } else if (match(TokenType.DIVIDE) && match(TokenType.MULTIPLY)) {
                while (pos < size - 1 && !checkType(0, TokenType.EOF)) {
                    if (match(TokenType.MULTIPLY) && match(TokenType.DIVIDE)) break;

                    match(getType());
                }
                if (pos == size - 1 || checkType(0, TokenType.EOF)) break;
            }

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
        } else if (checkType(0, TokenType.IDENTIFIER) && (checkType(1, TokenType.PLUSPLUS) || checkType(1, TokenType.MINUSMINUS))) {
            return incrementDecrement();
        } else if (match(TokenType.FUNC)) {
            return funcDeclaration();
        } else if (match(TokenType.IDENTIFIER) && match(TokenType.LEFT_PARENTHESIS)) {
            return funcCall();
        } else {
            new RuntimeError("Unexpected token: " + get(0).getValue(), fileName, get(0).getLine(), get(0).getPosition());
            return null;
        }
    }

    private Statement funcCall() {
        String name = get(-2).getValue();
        ArrayList<Expression> args = new ArrayList<>();
        while (!match(TokenType.RIGHT_PARENTHESIS) && pos < size - 1 && !checkType(0, TokenType.EOF)) {
            if (match(TokenType.COMMA)) continue;
            Expression expression = getExpression();
            args.add(expression);
        }
        skip(TokenType.SEMICOLON);

        return new FuncCallStatement(name, args, fileName, get(-2).getLine(), get(-2).getPosition());
    }

    private Statement funcDeclaration() {
        skip(TokenType.IDENTIFIER);
        String name = get(-1).getValue();
        if (FunctionManager.isFunction(name)) {
            new RuntimeError("Function " + name + " already exists", fileName, get(-1).getLine(), get(-1).getPosition());
            return null;
        }

        skip(TokenType.COLON);
        skip(TokenType.COLON);
        skip(TokenType.LEFT_PARENTHESIS);
        HashMap<String, TokenType> args = new HashMap<>();
        while (!match(TokenType.RIGHT_PARENTHESIS) && !checkType(0, TokenType.EOF)) {
            if (match(TokenType.STRING) || match(TokenType.INT) || match(TokenType.BOOL) || match(TokenType.FLOAT) || match(TokenType.CHAR)) {
                TokenType type = get(-1).getType();
                skip(TokenType.COLON);
                skip(TokenType.IDENTIFIER);
                String paramName = get(-1).getValue();
                args.put(paramName, type);
            } else {
                new SyntaxError("Unexpected token " + get(0).getValue(), fileName, get(0).getLine(), get(0).getPosition());
            }
        }
        skip(TokenType.COLON);
        if (!Types.isType(getType()) && !checkType(0, TokenType.VOID)) {
            new SyntaxError("Unexpected return type " + get(0).getValue(), fileName, get(0).getLine(), get(0).getPosition());
        }
        TokenType returnType = getType();
        match(returnType);
        BlockStatement block = getBlock();
        FunctionManager.addFunction(new Function(name, args, returnType, block));
        return new FunctionDeclarationStatement(name, args, returnType, block);
    }

    private Statement incrementDecrement() {
        match(TokenType.IDENTIFIER);
        match(getType());
        String name = get(-2).getValue();
        Value var = VariableManager.getVariable(name);
        if (var == null) {
            new RuntimeError("Variable " + name + " is not defined", fileName, get(-2).getLine(), get(-2).getPosition());
            return null;
        } else {
            if (var.getType() != TokenType.INT && var.getType() != TokenType.FLOAT) {
                new RuntimeError("Variable " + name + " is not a number", fileName, get(-2).getLine(), get(-2).getPosition());
                return null;
            } else {
                if (get(-1).getType() == TokenType.PLUSPLUS) {
                    skip(TokenType.SEMICOLON);
                    return new VariableAssignmentStatement(name, new OperationExpression(new ValueExpression(var, TokenType.INT), new ValueExpression(1, TokenType.INT), TokenType.PLUS).eval());
                } else {
                    skip(TokenType.SEMICOLON);
                    return new VariableAssignmentStatement(name, new OperationExpression(new ValueExpression(var, TokenType.INT), new ValueExpression(1, TokenType.INT), TokenType.MINUS).eval());
                }
            }
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
            expr = new VariableCallExpression(token.getValue(), fileName, get(0).getLine(), get(0).getPosition());
        } else if (match(TokenType.LEFT_PARENTHESIS)) {
            ConditionalExpression expression = getConditionalExpression();
            skip(TokenType.RIGHT_PARENTHESIS);
            return expression;
        } else if (match(TokenType.EXCLAMATION) && (match(TokenType.IDENTIFIER) || match(TokenType.EXCLAMATION) || match(TokenType.BOOL))) {
            boolean value = false;
            boolean exceptedValue = false;

            if (get(-1).getType() == TokenType.EXCLAMATION) {
                while (match(TokenType.EXCLAMATION) && pos < size - 1 && !checkType(0, TokenType.EOF)) {
                    exceptedValue = !exceptedValue;
                }
                if (!match(TokenType.IDENTIFIER) && !match(TokenType.EXCLAMATION) && !match(TokenType.BOOL)) new RuntimeError("Unexpected token " + get(-1).getValue(), fileName, get(-1).getLine(), get(-1).getPosition());
            }

            if (get(-1).getType() == TokenType.IDENTIFIER) {
                if (VariableManager.getVariable(get(-1).getValue()) == null) {
                    new RuntimeError("Variable " + get(-1).getValue() + " is not declared", fileName, get(-1).getLine(), get(-1).getPosition());
                    return null;
                }
                value = !Boolean.parseBoolean(VariableManager.getVariable(get(-1).getValue()).toString());
            } else {
                value = !Boolean.parseBoolean(get(-1).getValue());
            }

            expr = new ValueExpression(value == exceptedValue, TokenType.BOOL);
        } else new RuntimeError("Unknown expression : " + token.getValue(), fileName, token.getLine(), token.getPosition());

        if (Operators.isOperator(get(0).getType())) {
            return getOperationExpression(expr);
        } else return expr;
    }

    private BlockStatement getBlock() {
        skip(TokenType.MINUS);
        skip(TokenType.GREATER);
        ArrayList<Statement> statements = new ArrayList<>();
        skip(TokenType.LEFT_BRACE);
        while (!checkType(0, TokenType.RIGHT_BRACE) && !checkType(0, TokenType.EOF) && pos < size - 1) {
            Statement statement = getStatement();
            if (statement != null) {
                statements.add(statement);
            }
        }
        if (!match(TokenType.RIGHT_BRACE)) new RuntimeError("Missing }", fileName, get(-1).getLine(), get(-1).getPosition());
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
            new RuntimeError("Unknown expression : " + get(0).getValue(), fileName, get(0).getLine(), get(0).getPosition());
            return null;
        }
        Token operator = get(0);
        if (!Operators.isOperator(operator.getType())) {
            new RuntimeError("Unknown operator : " + operator.getValue(), fileName, operator.getLine(), operator.getPosition());
            return null;
        }
        match(operator.getType());
        Expression right = getExpression();
        if (right == null) {
            new RuntimeError("Unknown expression : " + get(0).getValue(), fileName, get(0).getLine(), get(0).getPosition());
            return null;
        }

        if (right.eval().getType() != left.eval().getType() && (!Arrays.asList(new TokenType[]{TokenType.INT, TokenType.FLOAT}).contains(right.eval().getType()) || !Arrays.asList(new TokenType[]{TokenType.INT, TokenType.FLOAT}).contains(left.eval().getType()))) {
            new RuntimeError("Type mismatch : " + left.eval().getType().toString().toLowerCase() + " and " + right.eval().getType().toString().toLowerCase(), fileName, get(0).getLine(), get(0).getPosition());
            return null;
        }

        return new OperationExpression(left, right, operator.getType());
    }

    private Statement ifElse() {
        if (!checkType(0, TokenType.LEFT_PARENTHESIS)) {
            new RuntimeError("Missing (", fileName, get(-1).getLine(), get(-1).getPosition());
            return null;
        }
        Expression condition = getExpression();
        BlockStatement ifBlock = getBlock();
        if (match(TokenType.ELSE)) {
            if (match(TokenType.IF)) {
                return new IfElseStatement(condition, ifBlock, ifElse());
            } else {
                BlockStatement elseBlock = getBlock();
                return new IfElseStatement(condition, ifBlock, elseBlock);
            }
        } else return new IfElseStatement(condition, ifBlock, null);
    }

    private Statement variableAssignment() {
        String name = get(0).getValue();
        match(TokenType.IDENTIFIER);
        skip(TokenType.EQUAL);
        Expression expr = getExpression();
        if (expr == null) {
            new RuntimeError("Unknown expression : " + get(0).getValue(), fileName, get(0).getLine(), get(0).getPosition());
            return null;
        }

        Value var = VariableManager.getVariable(name);
        if (var == null) {
            new RuntimeError("Variable " + name + " is not declared", fileName, get(0).getLine(), get(0).getPosition());
            return null;
        }
        if (var.getType() != expr.eval().getType()) {
            new RuntimeError("Type mismatch : " + var.getType().toString().toLowerCase() + " and " + expr.eval().getType().toString().toLowerCase(), fileName, get(0).getLine(), get(0).getPosition());
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
            new RuntimeError("Excepted expression after " + name.getValue(), fileName, name.getLine(), get(0).getPosition());
            return null;
        }

        Value var = VariableManager.getVariable(name.getValue());
        if (var != null) {
            new RuntimeError("Variable " + name.getValue() + " is already declared", fileName, get(0).getLine(), get(0).getPosition());
            return null;
        }

        if (expression.eval().getType() != type.getType()) {
            new RuntimeError("Excepted type " + type.getValue() + " but got " + expression.eval().getType().toString().toLowerCase(), fileName, get(0).getLine(), get(0).getPosition() - 1);
            return null;
        }

        skip(TokenType.SEMICOLON);
        return new VariableDeclarationStatement(new VariableExpression(name.getValue(), expression.eval()));
    }

    private ShowStatement show() {
        skip(TokenType.LEFT_PARENTHESIS);
        ArrayList<Expression> expressions = new ArrayList<>();
        while (!match(TokenType.RIGHT_PARENTHESIS)) {
            Expression expr = getExpression();
            if (expr == null) {
                new RuntimeError("Unknown expression: " + get(0).getValue(), fileName, get(0).getLine(), get(0).getPosition());
            }
            expressions.add(expr);
            if (!match(TokenType.COMMA) && !checkType(0, TokenType.RIGHT_PARENTHESIS) && !checkType(1, TokenType.RIGHT_PARENTHESIS)) {
                new SyntaxError("Excepted ',' for separate parameters", fileName, get(0).getLine(), get(0).getPosition());
            }
        }
        skip(TokenType.SEMICOLON);
        return new ShowStatement(expressions);
    }

    private void skip(TokenType type) {
        if (get(0).getType() != type) {
            if (type == TokenType.SEMICOLON) {
                new SemiColonError(fileName, get(0).getLine(), get(0).getPosition());
            } else new SyntaxError("Excepted " + type.getName() + " but got " + get(0).getType().getName(), fileName, get(0).getLine(), get(0).getPosition());
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
