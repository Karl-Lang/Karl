package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

public abstract class Value {
    public abstract String toString();

    public abstract int toInt();

    public abstract float toFloat();

    public abstract TokenType getType();
}
