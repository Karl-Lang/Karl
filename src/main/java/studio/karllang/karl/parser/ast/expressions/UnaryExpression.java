package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.values.BooleanValue;
import studio.karllang.karl.parser.ast.values.IntValue;
import studio.karllang.karl.parser.ast.values.Value;

public class UnaryExpression extends Expression {
    private final Expression expression;
    private final TokenType operator;
    private final File file;
    private final int line;
    private final int pos;

    public UnaryExpression(TokenType operator, Expression expression, File file, int line, int pos) {
        this.expression = expression;
        this.operator = operator;
        this.file = file;
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
                new RuntimeError("Unknown operator: " + operator, file.getStringPath(), line, pos);
                yield null;
            }
        };
    }
}
