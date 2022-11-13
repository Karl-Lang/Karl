package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.ast.expressions.VariableExpression;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.VariableManager;

public class VariableAssignmentStatement extends Statement {
    public final Value value;
    private final String name;

    public VariableAssignmentStatement(String name, Value value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void eval() {
        VariableManager.setVariable(name, value);
    }
}
