package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.expressions.Expression;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.Types;
import fr.aiko.Karl.std.VariableManager;

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
        Value var = VariableManager.getVariable(name);

        if (var == null) {
            new RuntimeError("Variable " + name + " is not declared", fileName, line, pos);
        }

        if (VariableManager.isFinal(name)) {
            new RuntimeError("Variable " + name + " is final", fileName, line, pos);
        }

        assert var != null;
        if (Types.checkValueType(var.getType(), value.getType()) || (var.getType() == TokenType.STRING && value.getType() == TokenType.NULL)) {
            VariableManager.setVariable(name, value, false);
        } else {
            new RuntimeError("Incorrect type for variable " + name + ": except " + Types.getTypeName(var.getType()) + " but got type " + Types.getTypeName(value.getType()), fileName, line, pos);
        }
    }
}
