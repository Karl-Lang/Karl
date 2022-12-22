package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.lexer.TokenType;
import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.lib.Types;
import studio.karllang.karl.lib.VariableManager;

public class VariableAssignmentStatement extends Statement {
    private final Expression expression;
    private final String name;
    private final int line;
    private final int pos;

    public VariableAssignmentStatement(String name, Expression expression, int line, int pos) {
        this.expression = expression;
        this.name = name;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public void eval() throws RuntimeError {
        Value value = expression.eval();
        Value var = VariableManager.getVariable(name);

        if (var == null) {
            throw new RuntimeError("Variable " + name + " is not declared", pos, line, printString());
        }

        if (VariableManager.isFinal(name)) {
            throw new RuntimeError("Variable " + name + " is final", pos, line, printString());
        }

        if (var.getType() == value.getType() || (var.getType() == TokenType.STRING && value.getType() == TokenType.NULL)) {
            VariableManager.setVariable(name, value, false);
        } else {
            throw new RuntimeError("Incorrect type for variable " + name + ": except " + Types.getTypeName(var.getType()) + " but got type " + Types.getTypeName(value.getType()), pos, line, printString());
        }
    }

    private String printString() throws RuntimeError {
        return name + " = " + expression.eval().toString();
    }
}
