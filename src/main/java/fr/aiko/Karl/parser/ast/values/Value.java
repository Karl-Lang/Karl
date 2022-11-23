package fr.aiko.Karl.parser.ast.values;

import fr.aiko.Karl.parser.TokenType;

public abstract class Value {
    public abstract String toString();

    public abstract int toInt();

    public abstract float toFloat();

    public abstract TokenType getType();
}
