package studio.karllang.karl.parser;

import studio.karllang.karl.errors.runtime.NumberError;
import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.errors.syntax.SyntaxError;
import studio.karllang.karl.lexer.Token;
import studio.karllang.karl.lexer.TokenType;
import studio.karllang.karl.parser.ast.expressions.*;
import studio.karllang.karl.parser.ast.statements.*;
import studio.karllang.karl.lib.*;

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

    public ArrayList<Statement> parse() throws RuntimeError, SyntaxError {
        while (pos < size - 1 && !checkType(0, TokenType.EOF)) {
            Statement statement = getStatement();
            if (statement != null) {
                statements.add(statement);
            }
        }
        return statements;
    }

    private Statement getStatement() throws RuntimeError, SyntaxError {
        if (match(TokenType.SHOW)) {
            return show();
        }
        if (match(TokenType.FINAL) && Types.contains(getType()) && getType() != TokenType.NULL) {
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
        } else
            throw new RuntimeError("Unexpected token: " + get(0).getValue(), get(0).getPosition(), get(0).getLine(), get(0).getValue());
    }

    private Statement funcCall() throws RuntimeError, SyntaxError {
        String name = get(-2).getValue();
        ArrayList<Expression> args = new ArrayList<>();
        while (!match(TokenType.RIGHT_PARENTHESIS) && pos < size - 1 && !checkType(0, TokenType.EOF)) {
            if (match(TokenType.COMMA)) continue;
            Expression expression = getExpression();
            args.add(expression);
        }
        skip(TokenType.SEMICOLON);

        return new FuncCallStatement(new FuncCallExpression(name, args, get(-2).getLine(), get(-2).getPosition()));
    }

    private Statement funcDeclaration() throws RuntimeError, SyntaxError {
        String name = get(0).getValue();
        if (ForbiddenNames.isForbiddenName(name)) {
            throw new RuntimeError("Function name " + name + " is forbidden", get(-1).getPosition(), get(-1).getLine(), "func " + name);
        }
        skip(TokenType.IDENTIFIER);
        if (FunctionManager.isFunction(name)) {
            throw new RuntimeError("Function " + name + " already exists", get(-1).getPosition(), get(-1).getLine(), "func " + name);
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
                    throw new RuntimeError("Parameter " + paramName + " already exists", get(-1).getPosition(), get(-1).getLine(), "func " + name + "::(" + type + ": " + paramName);
                }
                args.put(paramName, type);
            } else {
                throw new SyntaxError("Unexpected token " + get(0).getValue(), get(0).getPosition(), get(0).getLine(), get(0).getValue());
            }
            match(TokenType.COMMA);
        }
        skip(TokenType.COLON);
        if (!Types.isType(getType()) && !checkType(0, TokenType.VOID)) {
            throw new SyntaxError("Unexpected return type " + get(0).getValue(), get(0).getPosition(), get(0).getLine(), "return " + get(0).getValue());
        }
        TokenType returnType = getType();
        match(returnType);
        BlockStatement block = getBlock();
        FunctionManager.addFunction(new Function(name, args, returnType, block));

        return new FunctionDeclarationStatement(name, args, returnType, block);
    }

    private Statement incrementDecrement() throws SyntaxError {
        Token nameToken = get(0);
        match(TokenType.IDENTIFIER);
        String name = nameToken.getValue();

        Token operator = get(0);
        match(getType());
        if (operator.getType() == TokenType.PLUSPLUS) {
            skip(TokenType.SEMICOLON);
            return new IncrementDecrementStatement(name, TokenType.PLUS, nameToken.getLine(), nameToken.getPosition());
        } else {
            skip(TokenType.SEMICOLON);
            return new IncrementDecrementStatement(name, TokenType.MINUS, nameToken.getLine(), nameToken.getPosition());
        }
    }

    private BlockStatement getBlock() throws RuntimeError, SyntaxError {
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
            throw new RuntimeError("Missing }", get(-1).getPosition(), get(-1).getLine(), get(-1).getValue());
        return new BlockStatement(statements);
    }

    private Expression getExpression() throws RuntimeError, SyntaxError {
        Token token = get(0);
        Expression expression;
        if ((getType() == TokenType.IDENTIFIER && checkType(1, TokenType.LEFT_PARENTHESIS)) || (getType() == TokenType.IDENTIFIER) || (Types.isValueType(getType())) || (getType() == TokenType.EXCLAMATION)) {
            expression = getValue();
        } else if (match(TokenType.LEFT_PARENTHESIS)) {
            expression = getExpression();
            skip(TokenType.RIGHT_PARENTHESIS);
        } else
            throw new RuntimeError("Unknown expression : " + token.getValue(), token.getPosition(), token.getLine(), token.getValue());

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
                expression = new BinaryExpression(expression, right, operator, token.getLine(), token.getPosition());
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
                expression = new LogicalExpression(operator, expression, right, token.getLine(), token.getPosition());
            }
        }

        return expression;
    }

    private Expression getValue() throws RuntimeError, SyntaxError {
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

            return new FuncCallExpression(name, args, nameToken.getLine(), nameToken.getPosition());
        } else if (match(TokenType.EXCLAMATION)) {
            if (getType() != TokenType.IDENTIFIER && getType() != TokenType.BOOL_VALUE && getType() != TokenType.LEFT_PARENTHESIS && getType() != TokenType.EXCLAMATION)
                throw new RuntimeError("Unexpected token " + get(-1).getValue(), get(-1).getPosition(), get(-1).getLine(), "!" + get(-1).getValue());

            Expression expr;
            if (match(TokenType.LEFT_PARENTHESIS)) {
                expr = getExpression();
                skip(TokenType.RIGHT_PARENTHESIS);
            } else expr = getValue();

            return new UnaryExpression(TokenType.EXCLAMATION, expr, get(-1).getLine(), get(-1).getPosition());
        } else if (match(TokenType.STR_VALUE) || match(TokenType.INT_VALUE) || match(TokenType.BOOL_VALUE) || match(TokenType.FLOAT_VALUE) || match(TokenType.CHAR_VALUE) || match(TokenType.NULL)) {
            Token token = get(-1);
            return switch (token.getType()) {
                case STR_VALUE -> new ValueExpression(token.getValue(), token.getType());
                case INT_VALUE -> {
                    try {
                        yield new ValueExpression(Integer.parseInt(token.getValue()), token.getType());
                    } catch (NumberFormatException e) {
                        throw new NumberError(token.getPosition(), token.getLine(), token.getValue());
                    }
                }
                case BOOL_VALUE -> new ValueExpression(Boolean.parseBoolean(token.getValue()), token.getType());
                case FLOAT_VALUE -> {
                    try {
                        yield new ValueExpression(Float.parseFloat(token.getValue()), token.getType());
                    } catch (NumberFormatException e) {
                        throw new NumberError(token.getPosition(), token.getLine(), token.getValue());
                    }
                }
                case CHAR_VALUE -> new ValueExpression(token.getValue().charAt(0), token.getType());
                case NULL -> new ValueExpression((String) null, token.getType());
                default -> null;
            };
        } else if (match(TokenType.IDENTIFIER)) {
            return new VariableCallExpression(get(-1).getValue(), get(0).getLine(), get(0).getPosition());
        } else throw new RuntimeError("Unknown expression : " + get(-1).getValue(), get(-1).getPosition(), get(-1).getLine(), get(-1).getValue());
    }

    private Statement ifElse() throws RuntimeError, SyntaxError {
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

    private Statement variableAssignment() throws RuntimeError, SyntaxError {
        String name = get(0).getValue();
        match(TokenType.IDENTIFIER);
        skip(TokenType.EQUAL);

        Expression expr = getExpression();

        if (expr == null)
            throw new RuntimeError("Unknown expression : " + get(0).getValue(), get(0).getPosition(), get(0).getLine(), get(0).getValue());
        skip(TokenType.SEMICOLON);

        return new VariableAssignmentStatement(name, expr, get(0).getLine(), get(0).getPosition());
    }

    private Statement variableDeclaration(boolean isFinal) throws RuntimeError, SyntaxError {
        Token type = get(0);
        match(type.getType());
        skip(TokenType.COLON);
        Token name = get(0);

        skip(TokenType.IDENTIFIER);
        skip(TokenType.EQUAL);
        Expression expression = getExpression();
        skip(TokenType.SEMICOLON);

        if (expression == null)
            throw new RuntimeError("Expected expression after " + name.getValue(), get(0).getPosition(), name.getLine(), type + ": " + name.getValue() + " = " + get(0).getValue());

        return new VariableDeclarationStatement(expression, name.getValue(), type, name.getLine(), name.getPosition(), isFinal);
    }

    private ShowStatement show() throws RuntimeError, SyntaxError {
        skip(TokenType.LEFT_PARENTHESIS);
        ArrayList<Expression> expressions = new ArrayList<>();
        while (!match(TokenType.RIGHT_PARENTHESIS)) {
            Expression expr = getExpression();
            if (expr == null)
                throw new RuntimeError("Unknown expression at show statement: " + get(0).getValue(), get(0).getPosition(), get(0).getLine(), get(0).getValue());
            expressions.add(expr);
            if (!match(TokenType.COMMA) && !checkType(0, TokenType.RIGHT_PARENTHESIS) && !checkType(1, TokenType.RIGHT_PARENTHESIS))
                throw new RuntimeError("Excepted ',' for separate parameters", get(0).getPosition(), get(0).getLine(), get(0).getValue());
        }
        skip(TokenType.SEMICOLON);

        return new ShowStatement(expressions);
    }

    private void skip(TokenType type) throws SyntaxError {
        if (get(0).getType() != type) {
            if (type == TokenType.SEMICOLON) {
                throw new SyntaxError("Missing semicolon", get(-1).getPosition(), get(-1).getLine(), get(-1).getValue());
            } else
                throw new SyntaxError("Excepted " + Types.getTypeName(type) + " but got " + Types.getTypeName(get(0).getType()), get(0).getPosition(), get(0).getLine(), get(0).getValue());
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
