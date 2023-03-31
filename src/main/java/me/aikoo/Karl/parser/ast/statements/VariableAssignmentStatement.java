package me.aikoo.Karl.parser.ast.statements;

import me.aikoo.Karl.errors.RuntimeError.RuntimeError;
import me.aikoo.Karl.parser.TokenType;
import me.aikoo.Karl.parser.ast.expressions.Expression;
import me.aikoo.Karl.parser.ast.values.Value;
import me.aikoo.Karl.std.Types;
import me.aikoo.Karl.std.VariableManager;

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
    public void eval() {
        Value value = expression.eval();
        Value var = VariableManager.getCurrentFile().getVariable(name);

        if (var == null) {
            new RuntimeError("Variable " + name + " is not declared", fileName, line, pos);
        }

        if (VariableManager.getCurrentFile().isFinal(name)) {
            new RuntimeError("Variable " + name + " is final", fileName, line, pos);
        }

        assert var != null;
        if (var.getType() == value.getType() || (var.getType() == TokenType.STRING && value.getType() == TokenType.NULL)) {
            VariableManager.getCurrentFile().setVariable(name, value, false);
        } else {
            new RuntimeError("Incorrect type for variable " + name + ": except " + Types.getTypeName(var.getType()) + " but got type " + Types.getTypeName(value.getType()), fileName, line, pos);
        }
    }
}
