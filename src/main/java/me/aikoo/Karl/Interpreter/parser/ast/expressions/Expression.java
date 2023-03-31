package me.aikoo.Karl.Interpreter.parser.ast.expressions;

import me.aikoo.Karl.Interpreter.parser.ast.values.Value;

public abstract class Expression {
    public abstract Value eval();
}
