package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.values.FloatValue;
import fr.aiko.Karl.parser.ast.values.IntValue;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.Types;

public class BinaryExpression extends Expression {
    private final Expression left;
    private final Expression right;
    private final TokenType operator;
    private final String fileName;
    private final int line;
    private final int pos;

    public BinaryExpression(Expression left, Expression right, TokenType operator, String fileName, int line, int pos) {
        this.left = left;
        this.right = right;
        this.operator = operator;
        this.fileName = fileName;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public Value eval() {

        Value leftValue = left.eval();
        Value rightValue = right.eval();

        if ((leftValue.getType() == TokenType.INT_VALUE || leftValue.getType() == TokenType.FLOAT_VALUE) && (rightValue.getType() == TokenType.INT_VALUE || rightValue.getType() == TokenType.FLOAT_VALUE)) {
            return switch (operator) {
                case PLUS -> add(leftValue, rightValue);
                case MINUS -> sub(leftValue, rightValue);
                case MULTIPLY -> multiply(leftValue, rightValue);
                case DIVIDE -> divide(leftValue, rightValue);
                case MODULO -> modulo(leftValue, rightValue);
                default -> {
                    new RuntimeError("Bad operator: " + operator.getName(), fileName, line, pos);
                    yield null;
                }
            };
        } else if (leftValue.getType() == TokenType.STR_VALUE || rightValue.getType() == TokenType.STR_VALUE) {
            return switch (operator) {
                case PLUS -> new fr.aiko.Karl.parser.ast.values.StringValue(leftValue + rightValue.toString());
                default -> {
                    new RuntimeError("Bad operator: " + operator.getName(), fileName, line, pos);
                    yield null;
                }
            };
        } else {
            new RuntimeError("Unauthorized types for operation " + Types.getTypeName(leftValue.getType()) + " and " + Types.getTypeName(rightValue.getType()) , fileName, line, pos);
            return null;
        }
    }

    private Value divide(Value leftValue, Value rightValue) {
        float result = leftValue.toFloat() / rightValue.toFloat();
        if (result % 1 == 0) {
            return new IntValue((int) result);
        } else {
            return new FloatValue(result);
        }
    }

    private Value multiply(Value leftValue, Value rightValue) {
        float result = leftValue.toFloat() * rightValue.toFloat();
        if (result % 1 == 0) {
            return new IntValue((int) result);
        } else {
            return new FloatValue(result);
        }
    }

    private Value modulo(Value leftValue, Value rightValue) {
        float result = leftValue.toFloat() % rightValue.toFloat();
        if (result % 1 == 0) {
            return new IntValue((int) result);
        } else {
            return new FloatValue(result);
        }
    }

    private Value sub(Value leftValue, Value rightValue) {
        float result = leftValue.toFloat() - rightValue.toFloat();
        if (result % 1 == 0) {
            return new IntValue((int) result);
        } else {
            return new FloatValue(result);
        }
    }

    private Value add(Value leftValue, Value rightValue) {
        float result = leftValue.toFloat() + rightValue.toFloat();
        if (result % 1 == 0) {
            return new IntValue((int) result);
        } else {
            return new FloatValue(result);
        }
    }
}
