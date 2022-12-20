package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.olderrors.runtime.RuntimeOldError;
import studio.karllang.karl.lexer.TokenType;
import studio.karllang.karl.parser.ast.values.BooleanValue;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.LogicalOperators;

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
    public Value eval() throws RuntimeError {
        Value leftValue = left.eval();
        if (operator != null) {
            if (right != null) {
                Value rightValue = right.eval();
                final boolean isLeftNull = leftValue.getType() == TokenType.NULL;
                final boolean isRightNull = rightValue.getType() == TokenType.NULL;
                if (isLeftNull || isRightNull) {
                    return switch (operator) {
                        case NOT_EQUAL -> new BooleanValue(isLeftNull != isRightNull);
                        case EQUALEQUAL -> new BooleanValue(isLeftNull && isRightNull);
                        default -> {
                            throw new RuntimeError("Bad operator: " + operator.getName(), pos, line, printString());
                        }
                    };

                } else if ((leftValue.getType() != TokenType.INT_VALUE && leftValue.getType() != TokenType.FLOAT_VALUE) || (rightValue.getType() != TokenType.INT_VALUE && rightValue.getType() != TokenType.FLOAT_VALUE)) {
                    final boolean equals = leftValue.toString().equals(rightValue.toString());

                    return switch (operator) {
                        case AND ->
                                new BooleanValue(LogicalOperators.and(Boolean.parseBoolean(leftValue.toString()), Boolean.parseBoolean(rightValue.toString())));
                        case OR ->
                                new BooleanValue(LogicalOperators.or(Boolean.parseBoolean(leftValue.toString()), Boolean.parseBoolean(rightValue.toString())));
                        case EQUALEQUAL -> new BooleanValue(equals);
                        case NOT_EQUAL -> new BooleanValue(!equals);
                        default -> {
                            throw new RuntimeError("Unknown operator: " + operator.getValue(), pos, line, printString());
                        }
                    };
                } else {
                    return new BooleanValue(LogicalOperators.compare(leftValue, rightValue, operator, fileName, line, pos));
                }
            } else {
                return new BooleanValue(LogicalOperators.not(Boolean.parseBoolean(leftValue.toString())));
            }
        } else {
            return leftValue;
        }
    }

    private String printString() throws RuntimeError {
        return left.eval().toString() + " " + operator.getValue() + " " + right.eval().toString();
    }
}