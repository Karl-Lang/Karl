package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.File;

public class VariableExpression extends Expression {
    public final Value value;
    private final String name;
    private final boolean isFinal;
    private final File file;
    private final boolean isDeclaration;
    private final int line;
    private final int pos;

    public VariableExpression(String name, Value value, boolean isFinal, File file, boolean isDeclaration, int line, int pos) {
        this.name = name;
        this.value = value;
        this.isFinal = isFinal;
        this.file = file;
        this.isDeclaration = isDeclaration;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public Value eval() {
        setValue(value);
        return getValue();
    }

    public synchronized Value getValue() {
        return this.file.getVariableManager().getVariable(name).getValue();
    }

    public synchronized void setValue(Value value) {
        this.file.getVariableManager().setVariable(name, value, isFinal, isDeclaration, line, pos);
    }
}
