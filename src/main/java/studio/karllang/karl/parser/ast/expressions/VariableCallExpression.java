package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.modules.File;

public class VariableCallExpression extends Expression {
    private final String name;
    private final String fileName;
    private final File file;
    private final int line;
    private final int pos;

    public VariableCallExpression(String name, File file, int line, int pos) {
        this.name = name;
        this.file = file;
        this.fileName = file.getName();
        this.line = line;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Value eval() {
        if (this.file.getVariableManager().getVariable(name) == null) {
            new RuntimeError("Variable " + name + " is not defined", file.getStringPath(), line, pos);
        }

        return this.file.getVariableManager().getVariable(name).getValue();
    }
}
