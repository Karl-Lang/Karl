package me.aikoo.Karl.Interpreter.parser.ast.values;

import me.aikoo.Karl.Interpreter.parser.TokenType;

public abstract class Value {
    public abstract String toString();

    public abstract int toInt();

    public abstract float toFloat();

    public abstract TokenType getType();
}
