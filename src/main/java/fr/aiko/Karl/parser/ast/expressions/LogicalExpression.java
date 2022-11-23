package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.values.BooleanValue;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.LogicalOperators;

public class LogicalExpression extends Expression {
    private final Expression left;
    private final Expression right;
    private final TokenType operator;
    private final String fileName;
    private final int line;
    private final int pos;

    public LogicalExpression(TokenType operator, Expression left, Expression right, String fileName, int line, int pos) {
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
        if (operator != null) {
            if (right != null) {
                Value rightValue = right.eval();

                if (leftValue.getType() != TokenType.INT && rightValue.getType() != TokenType.INT && leftValue.getType() != TokenType.FLOAT && rightValue.getType() != TokenType.FLOAT) {
                    return switch (operator) {
                        case AND ->
                                new BooleanValue(LogicalOperators.and(Boolean.parseBoolean(leftValue.toString()), Boolean.parseBoolean(rightValue.toString())));
                        case OR ->
                                new BooleanValue(LogicalOperators.or(Boolean.parseBoolean(leftValue.toString()), Boolean.parseBoolean(rightValue.toString())));
                        case EQUALEQUAL -> new BooleanValue(leftValue.equals(rightValue));
                        case NOT_EQUAL -> new BooleanValue(!leftValue.equals(rightValue));
                        default -> {
                            new RuntimeError("Unknown operator: " + operator, fileName, line, pos);
                            yield null;
                        }
                    };
                } else {
                    return new BooleanValue(LogicalOperators.compare(leftValue.toFloat(), rightValue.toFloat(), operator));
                }
            } else {
                if (operator == TokenType.EXCLAMATION) {
                    return new BooleanValue(!Boolean.parseBoolean(leftValue.toString()));
                } else {
                    new RuntimeError("Unknown operator: " + operator, fileName, line, pos);
                    return null;
                }
            }
        } else {
            return new BooleanValue(Boolean.parseBoolean(leftValue.toString()));
        }
    }
}
