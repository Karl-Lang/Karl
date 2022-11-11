package fr.aiko.Karl.parser.ast.values;

import fr.aiko.Karl.parser.TokenType;

public class FloatValue extends Value {
    private final float value;

    public FloatValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }

    @Override
    public int toInt() {
        return Integer.parseInt(toString());
    }

    @Override
    public float toFloat() {
        return value;
    }

    @Override
    public TokenType getType() {
        return TokenType.FLOAT;
    }
}
