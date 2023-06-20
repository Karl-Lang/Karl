package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.modules.Variable;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.values.FloatValue;
import studio.karllang.karl.parser.ast.values.IntValue;
import studio.karllang.karl.parser.ast.values.Value;

public class IncrementDecrementStatement extends Statement {
    private final String name;
    private final String fileName;
    private final File file;
    private final int line;
    private final int pos;
    private final TokenType increment;

    public IncrementDecrementStatement(String name, TokenType increment, File file, int line, int pos) {
        this.name = name;
        this.file = file;
        this.fileName = file.getName();
        this.line = line;
        this.pos = pos;
        this.increment = increment;
    }

    @Override
    public void eval() {
        Variable variable = this.file.getVariableManager().getVariable(name);

        if (variable == null) {
            new RuntimeError("Variable " + name + " is not defined", file.getStringPath(), line, pos);
        }

        assert variable != null;
        Value value = variable.getValue();

        if (this.file.getVariableManager().isFinal(name)) {
            new RuntimeError("Variable " + name + " is final", file.getStringPath(), line, pos);
        }

        if (value.getType() != TokenType.INT_VALUE && value.getType() != TokenType.FLOAT_VALUE) {
            new RuntimeError("Variable " + name + " is not a number", file.getStringPath(), line, pos);
        }

        boolean isFloat = value.getType() == TokenType.FLOAT_VALUE;

        if (increment == TokenType.PLUS) {
            this.file.getVariableManager().setVariable(name, isFloat ? new FloatValue(value.toFloat() + 1) : new IntValue(value.toInt() + 1), false, variable.isDeclaration(), line, pos);
        } else {
            this.file.getVariableManager().setVariable(name, isFloat ? new FloatValue(value.toFloat() - 1) : new IntValue(value.toInt() - 1), false, variable.isDeclaration(), line, pos);
        }
    }
}
