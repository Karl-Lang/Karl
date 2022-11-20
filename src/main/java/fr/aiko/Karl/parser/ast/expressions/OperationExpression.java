package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.values.FloatValue;
import fr.aiko.Karl.parser.ast.values.IntValue;
import fr.aiko.Karl.parser.ast.values.Value;

import java.util.Arrays;

public class OperationExpression extends Expression {
    private final Expression left;
    private final Expression right;
    private final TokenType operator;
    private final String fileName;
    private final int line;
    private final int pos;

    public OperationExpression(Expression left, Expression right, TokenType operator, String fileName, int line, int pos) {
        this.left = left;
        this.right = right;
        this.operator = operator;
        this.fileName = fileName;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public Value eval() {
        if (right.eval().getType() != left.eval().getType() && (!Arrays.asList(new TokenType[]{TokenType.INT, TokenType.FLOAT}).contains(right.eval().getType()) || !Arrays.asList(new TokenType[]{TokenType.INT, TokenType.FLOAT}).contains(left.eval().getType()))) {
            new RuntimeError("Type mismatch : " + left.eval().getType().toString().toLowerCase() + " and " + right.eval().getType().toString().toLowerCase(), fileName, line, pos);
            return null;
        }

        Value leftValue = left.eval();
        Value rightValue = right.eval();

        if (leftValue.getType() == TokenType.INT && rightValue.getType() == TokenType.INT) {
            return switch (operator) {
                case PLUS -> new IntValue(leftValue.toInt() + rightValue.toInt());
                case MINUS -> new IntValue(leftValue.toInt() - rightValue.toInt());
                case MULTIPLY -> new IntValue(leftValue.toInt() * rightValue.toInt());
                case DIVIDE -> new IntValue(leftValue.toInt() / rightValue.toInt());
                case MODULO -> new IntValue(leftValue.toInt() % rightValue.toInt());
                default -> throw new RuntimeException("Unknown operator: " + operator);
            };
        } else if (leftValue.getType() == TokenType.FLOAT || rightValue.getType() == TokenType.INT || leftValue.getType() == TokenType.INT || rightValue.getType() == TokenType.FLOAT) {
            return switch (operator) {
                case PLUS -> new FloatValue(leftValue.toFloat() + rightValue.toFloat());
                case MINUS -> new FloatValue(leftValue.toFloat() - rightValue.toFloat());
                case MULTIPLY -> new FloatValue(leftValue.toFloat() * rightValue.toFloat());
                case DIVIDE -> new FloatValue(leftValue.toFloat() / rightValue.toFloat());
                case MODULO -> new FloatValue(leftValue.toFloat() % rightValue.toFloat());
                default -> throw new RuntimeException("Unknown operator: " + operator);
            };
        } else {
            throw new RuntimeException("Unauthorized type for operation" + leftValue.getType());
        }
    }
}
