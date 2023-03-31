package me.aikoo.Karl.parser.ast.expressions;

import me.aikoo.Karl.parser.ast.values.Value;
import me.aikoo.Karl.std.VariableManager;

public class VariableExpression extends Expression {
    public final Value value;
    private final String name;
    private final boolean isFinal;

    public VariableExpression(String name, Value value, boolean isFinal) {
        this.name = name;
        this.value = value;
        this.isFinal = isFinal;
    }

    @Override
    public Value eval() {
        setValue(value);
        return getValue();
    }

    public synchronized Value getValue() {
        return VariableManager.getCurrentFile().getVariable(name);
    }

    public synchronized void setValue(Value value) {
        VariableManager.getCurrentFile().setVariable(name, value, isFinal);
    }
}
