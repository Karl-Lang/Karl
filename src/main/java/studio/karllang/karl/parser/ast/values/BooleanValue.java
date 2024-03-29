package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

public class BooleanValue extends Value {
    private final Boolean value;

    public BooleanValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int toInt() {
        return 0;
    }

    @Override
    public float toFloat() {
        return 0;
    }

    @Override
    public TokenType getType() {
        return TokenType.BOOL_VALUE;
    }
}
