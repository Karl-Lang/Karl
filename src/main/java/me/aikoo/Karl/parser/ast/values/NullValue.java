package me.aikoo.Karl.parser.ast.values;

import me.aikoo.Karl.parser.TokenType;

public class NullValue extends Value {
    public final String value;

    public NullValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
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
        return TokenType.NULL;
    }
}
