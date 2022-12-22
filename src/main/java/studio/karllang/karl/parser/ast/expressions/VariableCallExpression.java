package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.olderrors.runtime.RuntimeOldError;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.VariableManager;

public class VariableCallExpression extends Expression {
    private final String name;
    private final int line;
    private final int pos;

    public VariableCallExpression(String name, int line, int pos) {
        this.name = name;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Value eval() throws RuntimeError {
        if (VariableManager.getVariable(name) == null) {
            throw new RuntimeError("Variable " + name + " is not defined", pos, line, printString());
        }

        return VariableManager.getVariable(name);
    }

    private String printString() {
        return name;
    }
}
