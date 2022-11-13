package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.values.BooleanValue;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.LogicalOperators;

public class ConditionalExpression extends Expression {
    private final Expression left;
    private final Expression right;
    private final TokenType operator;

    public ConditionalExpression(TokenType operator, Expression left, Expression right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public Value eval() {
        Value leftValue = left.eval();
        if (operator != null) {
            if (right != null) {
                Value rightValue = right.eval();
                if (leftValue.getType() == TokenType.INT || leftValue.getType() == TokenType.FLOAT) {
                    return switch (operator) {
                        case EQUALEQUAL -> new BooleanValue(leftValue.toFloat() == rightValue.toFloat());
                        case NOT_EQUAL -> new BooleanValue(leftValue.toFloat() != rightValue.toFloat());
                        case GREATER -> new BooleanValue(leftValue.toFloat() > rightValue.toFloat());
                        case GREATER_EQUAL -> new BooleanValue(leftValue.toFloat() >= rightValue.toFloat());
                        case LESS -> new BooleanValue(leftValue.toFloat() < rightValue.toFloat());
                        case LESS_EQUAL -> new BooleanValue(leftValue.toFloat() <= rightValue.toFloat());
                        case AND ->
                                new BooleanValue(LogicalOperators.and(Boolean.parseBoolean(leftValue.toString()), Boolean.parseBoolean(rightValue.toString())));
                        case OR ->
                                new BooleanValue(LogicalOperators.or(Boolean.parseBoolean(leftValue.toString()), Boolean.parseBoolean(rightValue.toString())));
                        default -> throw new RuntimeException("Unknown operator: " + operator);
                    };
                } else {
                    final boolean equals = leftValue.toString().equals(rightValue.toString());
                    return switch (operator) {
                        case EQUALEQUAL -> new BooleanValue(equals);
                        case NOT_EQUAL -> new BooleanValue(!equals);
                        case AND ->
                                new BooleanValue(LogicalOperators.and(Boolean.parseBoolean(leftValue.toString()), Boolean.parseBoolean(rightValue.toString())));
                        case OR ->
                                new BooleanValue(LogicalOperators.or(Boolean.parseBoolean(leftValue.toString()), Boolean.parseBoolean(rightValue.toString())));
                        default -> throw new RuntimeException("Unknown operator: " + operator);
                    };
                }
            } else {
                return switch (operator) {
                    case EXCLAMATION -> new BooleanValue(!Boolean.parseBoolean(leftValue.toString()));
                    default -> throw new RuntimeException("Unknown operator: " + operator);
                };
            }
        } else {
            return new BooleanValue(Boolean.parseBoolean(leftValue.toString()));
        }
    }
}
