package studio.karllang.karl.parser;

import studio.karllang.karl.errors.RuntimeError.NumberError;
import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.errors.SyntaxError.SemiColonError;
import studio.karllang.karl.errors.SyntaxError.SyntaxError;
import studio.karllang.karl.parser.ast.expressions.*;
import studio.karllang.karl.parser.ast.statements.*;
import studio.karllang.karl.std.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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
        } if (match(TokenType.FINAL) && Types.contains(getType()) && getType() != TokenType.NULL) {
            return variableDeclaration(true);
        } else if (Types.contains(getType()) && getType() != TokenType.NULL) {
            return variableDeclaration(false);
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

        return new FuncCallStatement(new FuncCallExpression(name, args, fileName, get(-2).getLine(), get(-2).getPosition()));
    }

    private Statement funcDeclaration() {
        String name = get(0).getValue();
        if (ForbiddenNames.isForbiddenName(name)) {
            new RuntimeError("Function name " + name + " is forbidden", fileName, get(-1).getLine(), get(-1).getPosition());
        }
        skip(TokenType.IDENTIFIER);
        if (FunctionManager.getCurrentFile().isFunction(name)) {
            new RuntimeError("Function " + name + " already exists", fileName, get(-1).getLine(), get(-1).getPosition());
        }

        skip(TokenType.COLON);
        skip(TokenType.COLON);
        skip(TokenType.LEFT_PARENTHESIS);
        LinkedHashMap<String, TokenType> args = new LinkedHashMap<>();
        while (!match(TokenType.RIGHT_PARENTHESIS) && !checkType(0, TokenType.EOF)) {
            if (match(TokenType.STRING) || match(TokenType.INT) || match(TokenType.BOOL) || match(TokenType.FLOAT) || match(TokenType.CHAR)) {
                TokenType type = get(-1).getType();
                skip(TokenType.COLON);
                skip(TokenType.IDENTIFIER);
                String paramName = get(-1).getValue();
                if (args.containsKey(paramName)) {
                    new RuntimeError("Parameter " + paramName + " already exists", fileName, get(-1).getLine(), get(-1).getPosition());
                }
                args.put(paramName, type);
            } else {
                new SyntaxError("Unexpected token " + get(0).getValue(), fileName, get(0).getLine(), get(0).getPosition());
            }
            match(TokenType.COMMA);
        }
        skip(TokenType.COLON);
        if (!Types.isType(getType()) && !checkType(0, TokenType.VOID)) {
            new SyntaxError("Unexpected return type " + get(0).getValue(), fileName, get(0).getLine(), get(0).getPosition());
        }
        TokenType returnType = getType();
        match(returnType);
        BlockStatement block = getBlock();
        FunctionManager.getCurrentFile().addFunction(new Function(name, args, returnType, block));

        return new FunctionDeclarationStatement(name, args, returnType, block);
    }

    private Statement incrementDecrement() {
        Token nameToken = get(0);
        match(TokenType.IDENTIFIER);
        String name = nameToken.getValue();

        Token operator = get(0);
        match(getType());
        if (operator.getType() == TokenType.PLUSPLUS) {
            skip(TokenType.SEMICOLON);
            return new IncrementDecrementStatement(name, TokenType.PLUS, fileName, nameToken.getLine(), nameToken.getPosition());
        } else {
            skip(TokenType.SEMICOLON);
            return new IncrementDecrementStatement(name, TokenType.MINUS, fileName, nameToken.getLine(), nameToken.getPosition());
        }
    }

    private BlockStatement getBlock() {
        skip(TokenType.MINUS);
        skip(TokenType.GREATER);
        ArrayList<Statement> statements = new ArrayList<>();
        skip(TokenType.LEFT_BRACE);
        while (!checkType(0, TokenType.RIGHT_BRACE) && !checkType(0, TokenType.EOF) && pos < size - 1) {
            if (match(TokenType.RETURN)) {
                Expression expr = getExpression();
                skip(TokenType.SEMICOLON);
                statements.add(new ReturnStatement(expr));
            } else {
                Statement statement = getStatement();
                if (statement != null) {
                    statements.add(statement);
                }
            }
        }
        if (!match(TokenType.RIGHT_BRACE))
            new RuntimeError("Missing }", fileName, get(-1).getLine(), get(-1).getPosition());
        return new BlockStatement(statements);
    }

    private Expression getExpression() {
        Token token = get(0);
        Expression expression = null;
        if ((getType() == TokenType.IDENTIFIER && checkType(1, TokenType.LEFT_PARENTHESIS)) || (getType() == TokenType.IDENTIFIER) || (Types.isValueType(getType())) || (getType() == TokenType.EXCLAMATION)) {
            expression = getValue();
        } else if (match(TokenType.LEFT_PARENTHESIS)) {
            expression = getExpression();
            skip(TokenType.RIGHT_PARENTHESIS);
        } else
            new RuntimeError("Unknown expression : " + token.getValue(), fileName, token.getLine(), token.getPosition());

        // Binary operations
        if (Operators.isOperator(getType())) {
            while (Operators.isOperator(getType())) {
                TokenType operator = getType();
                skip(operator);
                int position = pos;
                Expression right = getExpression();
                if (!(right instanceof ValueExpression) && !(right instanceof BinaryExpression)) {
                    pos = position;
                    right = getValue();
                }
                expression = new BinaryExpression(expression, right, operator, fileName, token.getLine(), token.getPosition());
            }
        }

        // Logical operations
        if (LogicalOperators.isOperator(getType())) {
            while (LogicalOperators.isOperator(getType())) {
                TokenType operator = getType();
                skip(operator);
                Expression right;
                if (operator == TokenType.AND || operator == TokenType.OR) {
                    right = getExpression();
                } else {
                    right = getValue();
                }
                expression = new LogicalExpression(operator, expression, right, fileName, token.getLine(), token.getPosition());
            }
        }

        return expression;
    }

    private Expression getValue() {
        if (getType() == TokenType.IDENTIFIER && get(1).getType() == TokenType.LEFT_PARENTHESIS) {
            Token nameToken = get(0);
            String name = nameToken.getValue();
            match(TokenType.IDENTIFIER);
            match(TokenType.LEFT_PARENTHESIS);
            ArrayList<Expression> args = new ArrayList<>();
            while (!match(TokenType.RIGHT_PARENTHESIS) && pos < size - 1 && !checkType(0, TokenType.EOF)) {
                args.add(getExpression());
                match(TokenType.COMMA);
            }

            return new FuncCallExpression(name, args, fileName, nameToken.getLine(), nameToken.getPosition());
        } else if (match(TokenType.EXCLAMATION)) {
            if (getType() != TokenType.IDENTIFIER && getType() != TokenType.BOOL_VALUE && getType() != TokenType.LEFT_PARENTHESIS)
                new RuntimeError("Unexpected token " + get(-1).getValue(), fileName, get(-1).getLine(), get(-1).getPosition());

            Expression expr;
            if (match(TokenType.LEFT_PARENTHESIS)) {
                expr = getExpression();
                skip(TokenType.RIGHT_PARENTHESIS);
            } else expr = getValue();

            return new UnaryExpression(TokenType.EXCLAMATION, expr, fileName, get(-1).getLine(), get(-1).getPosition());
        } else if (match(TokenType.STR_VALUE) || match(TokenType.INT_VALUE) || match(TokenType.BOOL_VALUE) || match(TokenType.FLOAT_VALUE) || match(TokenType.CHAR_VALUE) || match(TokenType.NULL)) {
            Token token = get(-1);
            return switch (token.getType()) {
                case STR_VALUE -> new ValueExpression(token.getValue(), token.getType());
                case INT_VALUE -> {
                    try {
                        yield new ValueExpression(Integer.parseInt(token.getValue()), token.getType());
                    } catch (NumberFormatException e) {
                        new NumberError("Invalid number: " + token.getValue(), fileName, token.getLine(), token.getPosition());
                        yield null;
                    }
                }
                case BOOL_VALUE -> new ValueExpression(Boolean.parseBoolean(token.getValue()), token.getType());
                case FLOAT_VALUE -> {
                    try {
                        yield new ValueExpression(Float.parseFloat(token.getValue()), token.getType());
                    } catch (NumberFormatException e) {
                        new NumberError("Invalid number: " + token.getValue(), fileName, token.getLine(), token.getPosition());
                        yield null;
                    }
                }
                case CHAR_VALUE -> new ValueExpression(token.getValue().charAt(0), token.getType());
                case NULL -> new ValueExpression((String) null, token.getType());
                default -> null;
            };
        } else if (match(TokenType.IDENTIFIER)) {
            return new VariableCallExpression(get(-1).getValue(), fileName, get(0).getLine(), get(0).getPosition());
        } else {
            new RuntimeError("Unknown expression : " + get(-1).getValue(), fileName, get(-1).getLine(), get(-1).getPosition());
            return null;
        }
    }

    private Statement ifElse() {
        skip(TokenType.LEFT_PARENTHESIS);
        Expression condition = getExpression();
        skip(TokenType.RIGHT_PARENTHESIS);
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
        }

        skip(TokenType.SEMICOLON);

        assert expr != null;
        return new VariableAssignmentStatement(name, expr, fileName, get(0).getLine(), get(0).getPosition());
    }

    private Statement variableDeclaration(boolean isFinal) {
        Token type = get(0);
        match(type.getType());
        skip(TokenType.COLON);
        Token name = get(0);

        skip(TokenType.IDENTIFIER);
        skip(TokenType.EQUAL);
        Expression expression = getExpression();
        skip(TokenType.SEMICOLON);

        if (expression == null) {
            new RuntimeError("Expected expression after " + name.getValue(), fileName, name.getLine(), get(0).getPosition());
        }

        return new VariableDeclarationStatement(expression, name.getValue(), type, fileName, name.getLine(), name.getPosition(), isFinal);
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
                new SemiColonError(fileName, get(-1).getLine(), get(-1).getPosition());
            } else
                new SyntaxError("Excepted " + Types.getTypeName(type) + " but got " + Types.getTypeName(get(0).getType()), fileName, get(0).getLine(), get(0).getPosition());
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
