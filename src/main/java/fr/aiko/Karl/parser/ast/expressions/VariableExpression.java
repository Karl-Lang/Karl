package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.VariableManager;

public class VariableExpression extends Expression {
    public final Value value;
    private final String name;

    public VariableExpression(String name, Value value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Value eval() {
        setValue(value);
        return getValue();
    }

    public synchronized Value getValue() {
        return VariableManager.getVariable(name);
    }

    public synchronized void setValue(Value value) {
        VariableManager.setVariable(name, value);
    }
}
