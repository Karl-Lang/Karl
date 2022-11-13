package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.VariableManager;

public class VariableCallExpression extends Expression {
    private final String name;

    public VariableCallExpression(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Value eval() {
        return VariableManager.getVariable(name);
    }
}
