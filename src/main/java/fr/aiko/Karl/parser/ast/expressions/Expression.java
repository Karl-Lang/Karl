package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.parser.ast.values.Value;

public abstract class Expression {
    public abstract Value eval();
}
