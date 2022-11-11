package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.VariableManager;

public class VariableExpression extends Expression {
    private final String name;

    public VariableExpression(String name, Value value) {
        this.name = name;
        setValue(value);
    }

    @Override
    public Value eval() {
        return getValue();
    }

    public Value getValue() {
        if (VariableManager.getVariable(name) != null) {
            return VariableManager.getVariable(name);
        } else {
            return null;
        }
    }

    public Value setValue(Value value) {
        VariableManager.setVariable(name, value);
        return value;
    }
}
