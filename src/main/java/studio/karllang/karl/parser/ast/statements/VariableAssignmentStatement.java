package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.olderrors.runtime.RuntimeOldError;
import studio.karllang.karl.lexer.TokenType;
import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.Types;
import studio.karllang.karl.std.VariableManager;

public class VariableAssignmentStatement extends Statement {
    private final Expression expression;
    private final String name;
    private final String fileName;
    private final int line;
    private final int pos;

    public VariableAssignmentStatement(String name, Expression expression, String fileName, int line, int pos) {
        this.expression = expression;
        this.name = name;
        this.fileName = fileName;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public void eval() throws RuntimeError {
        Value value = expression.eval();
        Value var = VariableManager.getVariable(name);

        if (var == null) {
            new RuntimeOldError("Variable " + name + " is not declared", fileName, line, pos);
        }

        if (VariableManager.isFinal(name)) {
            new RuntimeOldError("Variable " + name + " is final", fileName, line, pos);
        }

        assert var != null;
        if (var.getType() == value.getType() || (var.getType() == TokenType.STRING && value.getType() == TokenType.NULL)) {
            VariableManager.setVariable(name, value, false);
        } else {
            new RuntimeOldError("Incorrect type for variable " + name + ": except " + Types.getTypeName(var.getType()) + " but got type " + Types.getTypeName(value.getType()), fileName, line, pos);
        }
    }
}
