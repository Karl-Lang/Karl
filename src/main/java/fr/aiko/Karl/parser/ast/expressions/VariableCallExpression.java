package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.VariableManager;

public class VariableCallExpression extends Expression {
    private final String name;
    private final String fileName;
    private final int line;
    private final int pos;

    public VariableCallExpression(String name, String fileName, int line, int pos) {
        this.name = name;
        this.fileName = fileName;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Value eval() {
        if (VariableManager.getVariable(name) == null) {
            System.out.println("Scope :" + VariableManager.getScope());
            new RuntimeError("Variable " + name + " is not defined", fileName, line, pos);
        }
        return VariableManager.getVariable(name);
    }
}
