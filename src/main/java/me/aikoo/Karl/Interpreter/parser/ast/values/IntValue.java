package me.aikoo.Karl.Interpreter.parser.ast.values;

import me.aikoo.Karl.Interpreter.parser.TokenType;

public class IntValue extends Value {
    private final int value;

    public IntValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int toInt() {
        return value;
    }

    @Override
    public float toFloat() {
        return Float.parseFloat(toString());
    }

    @Override
    public TokenType getType() {
        return TokenType.INT_VALUE;
    }
}
