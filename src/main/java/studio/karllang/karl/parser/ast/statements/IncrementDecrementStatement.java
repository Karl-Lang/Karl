package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.lexer.TokenType;
import studio.karllang.karl.parser.ast.values.FloatValue;
import studio.karllang.karl.parser.ast.values.IntValue;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.VariableManager;

public class IncrementDecrementStatement extends Statement {
    private final String name;
    private final int line;
    private final int pos;
    private final TokenType increment;

    public IncrementDecrementStatement(String name, TokenType increment, int line, int pos) {
        this.name = name;
        this.line = line;
        this.pos = pos;
        this.increment = increment;
    }

    @Override
    public void eval() throws RuntimeError {
        Value variable = VariableManager.getVariable(name);

        if (variable == null) {
            throw new RuntimeError("Variable " + name + " is not defined", pos, line, printString());
        }

        if (VariableManager.isFinal(name)) {
            throw new RuntimeError("Variable " + name + " is final", pos, line, printString());
        }

        if (variable.getType() != TokenType.INT_VALUE && variable.getType() != TokenType.FLOAT_VALUE) {
            throw new RuntimeError("Variable " + name + " is not a number", pos, line, printString());
        }

        boolean isFloat = variable.getType() == TokenType.FLOAT_VALUE;

        if (increment == TokenType.PLUS) {
            VariableManager.setVariable(name, isFloat ? new FloatValue(variable.toFloat() + 1) : new IntValue(variable.toInt() + 1), false);
        } else {
            VariableManager.setVariable(name, isFloat ? new FloatValue(variable.toFloat() - 1) : new IntValue(variable.toInt() - 1), false);
        }
    }

    private String printString() {
        return name + increment.getValue() + increment.getValue() + ";";
    }
}
