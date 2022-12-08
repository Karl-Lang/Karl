package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.values.FloatValue;
import fr.aiko.Karl.parser.ast.values.IntValue;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.VariableManager;

public class IncrementDecrementStatement extends Statement {
    private final String name;
    private final String fileName;
    private final int line;
    private final int pos;
    private final TokenType increment;

    public IncrementDecrementStatement(String name, TokenType increment, String fileName, int line, int pos) {
        this.name = name;
        this.fileName = fileName;
        this.line = line;
        this.pos = pos;
        this.increment = increment;
    }

    @Override
    public void eval() {
        Value variable = VariableManager.getVariable(name);

        if (variable == null) {
            new RuntimeError("Variable " + name + " is not defined", fileName, line, pos);
        }

        assert variable != null;

        if (variable.getType() != TokenType.INT_VALUE && variable.getType() != TokenType.FLOAT_VALUE) {
            new RuntimeError("Variable " + name + " is not a number", fileName, line, pos);
        }

        boolean isFloat = variable.getType() == TokenType.FLOAT_VALUE;

        if (increment == TokenType.PLUS) {
            VariableManager.setVariable(name, isFloat ? new FloatValue(variable.toFloat() + 1) : new IntValue(variable.toInt() + 1));
        } else {
            VariableManager.setVariable(name, isFloat ? new FloatValue(variable.toFloat() - 1) : new IntValue(variable.toInt() - 1));
        }
    }
}
