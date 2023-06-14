package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.File;
import studio.karllang.karl.std.Types;
import studio.karllang.karl.std.Variable;

public class VariableAssignmentStatement extends Statement {
    private final Expression expression;
    private final String name;
    private final String fileName;
    private final File file;
    private final int line;
    private final int pos;

    public VariableAssignmentStatement(String name, Expression expression, File file, int line, int pos) {
        this.expression = expression;
        this.name = name;
        this.file = file;
        this.fileName = file.getName();
        this.line = line;
        this.pos = pos;
    }

    @Override
    public void eval() {
        Value value = expression.eval();
        Variable var = this.file.getVariableManager().getVariable(name);

        if (var == null) {
            new RuntimeError("Variable " + name + " is not declared", file.getStringPath(), line, pos);
        }

        assert var != null;
        Value val = var.getValue();

        if (this.file.getVariableManager().isFinal(name)) {
            new RuntimeError("Variable " + name + " is final", file.getStringPath(), line, pos);
        }

        if (val.getType() == value.getType() || (val.getType() == TokenType.STRING && value.getType() == TokenType.NULL)) {
            this.file.getVariableManager().setVariable(name, value, false);
        } else {
            new RuntimeError("Incorrect type for variable " + name + ": except " + Types.getTypeName(val.getType()) + " but got type " + Types.getTypeName(value.getType()), file.getStringPath(), line, pos);
        }
    }
}
