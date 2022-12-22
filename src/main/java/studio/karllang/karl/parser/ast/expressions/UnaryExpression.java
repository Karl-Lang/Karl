package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.lexer.TokenType;
import studio.karllang.karl.parser.ast.values.BooleanValue;
import studio.karllang.karl.parser.ast.values.IntValue;
import studio.karllang.karl.parser.ast.values.Value;

public class UnaryExpression extends Expression {
    private final Expression expression;
    private final TokenType operator;
    private final int line;
    private final int pos;

    public UnaryExpression(TokenType operator, Expression expression, int line, int pos) {
        this.expression = expression;
        this.operator = operator;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public Value eval() throws RuntimeError {
        Value value = expression.eval();
        return switch (operator) {
            case EXCLAMATION -> new BooleanValue(!Boolean.parseBoolean(value.toString()));
            case MINUS -> new IntValue(-value.toInt());
            default -> throw new RuntimeError("Unknown operator: " + operator, pos, line, printString());
        };
    }

    private String printString() throws RuntimeError {
        return operator.getValue().toLowerCase() + expression.eval().toString();
    }
}
