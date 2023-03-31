package me.aikoo.Karl.parser.ast.expressions;

import me.aikoo.Karl.errors.RuntimeError.RuntimeError;
import me.aikoo.Karl.parser.TokenType;
import me.aikoo.Karl.parser.ast.values.BooleanValue;
import me.aikoo.Karl.parser.ast.values.IntValue;
import me.aikoo.Karl.parser.ast.values.Value;

public class UnaryExpression extends Expression {
    private final Expression expression;
    private final TokenType operator;
    private final String fileName;
    private final int line;
    private final int pos;

    public UnaryExpression(TokenType operator, Expression expression, String fileName, int line, int pos) {
        this.expression = expression;
        this.operator = operator;
        this.fileName = fileName;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public Value eval() {
        Value value = expression.eval();
        return switch (operator) {
            case EXCLAMATION -> new BooleanValue(!Boolean.parseBoolean(value.toString()));
            case MINUS -> new IntValue(-value.toInt());
            default -> {
                new RuntimeError("Unknown operator: " + operator, fileName, line, pos);
                yield null;
            }
        };
    }
}
