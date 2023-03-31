package me.aikoo.Karl.parser.ast.expressions;

import me.aikoo.Karl.parser.ast.values.Value;

public abstract class Expression {
    public abstract Value eval();
}
