package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.values.FloatValue;
import fr.aiko.Karl.parser.ast.values.IntValue;
import fr.aiko.Karl.parser.ast.values.StringValue;
import fr.aiko.Karl.parser.ast.values.Value;

import java.util.HashMap;

public class OperationExpression extends Expression {
    private final Expression left;
    private final Expression right;
    private final TokenType operator;

    public OperationExpression(Expression left, Expression right, TokenType operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public Value eval() {
        Value leftValue = left.eval();
        Value rightValue = right.eval();

        if (leftValue.getType() == TokenType.INT) {
            return switch (operator) {
                case PLUS -> new IntValue(leftValue.toInt() + rightValue.toInt());
                case MINUS -> new IntValue(leftValue.toInt() - rightValue.toInt());
                case MULTIPLY -> new IntValue(leftValue.toInt() * rightValue.toInt());
                case DIVIDE -> new IntValue(leftValue.toInt() / rightValue.toInt());
                case MODULO -> new IntValue(leftValue.toInt() % rightValue.toInt());
                default -> throw new RuntimeException("Unknown operator: " + operator);
            };
        } else if (leftValue.getType() == TokenType.FLOAT) {
            return switch (operator) {
                case PLUS -> new FloatValue(leftValue.toFloat() + rightValue.toFloat());
                case MINUS -> new FloatValue(leftValue.toFloat() - rightValue.toFloat());
                case MULTIPLY -> new FloatValue(leftValue.toFloat() * rightValue.toFloat());
                case DIVIDE -> new FloatValue(leftValue.toFloat() / rightValue.toFloat());
                case MODULO -> new FloatValue(leftValue.toFloat() % rightValue.toFloat());
                default -> throw new RuntimeException("Unknown operator: " + operator);
            };
        } else {
            throw new RuntimeException("Unknown type: " + leftValue.getType());
        }
    }
}
