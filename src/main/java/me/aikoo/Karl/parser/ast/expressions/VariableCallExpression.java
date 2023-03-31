package me.aikoo.Karl.parser.ast.expressions;

import me.aikoo.Karl.errors.RuntimeError.RuntimeError;
import me.aikoo.Karl.parser.ast.values.Value;
import me.aikoo.Karl.std.VariableManager;

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
        if (VariableManager.getCurrentFile().getVariable(name) == null) {
            new RuntimeError("Variable " + name + " is not defined", fileName, line, pos);
        }

        return VariableManager.getCurrentFile().getVariable(name);
    }
}
