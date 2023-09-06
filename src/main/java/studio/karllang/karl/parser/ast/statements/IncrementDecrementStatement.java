package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.values.FloatValue;
import studio.karllang.karl.parser.ast.values.IntValue;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.Variable;
import studio.karllang.karl.std.VariableManager;

public class IncrementDecrementStatement extends Statement {
  private final String name;
  private final String fileName;
  private final int line;
  private final int pos;
  private final TokenType increment;

  public IncrementDecrementStatement(
      String name, TokenType increment, String fileName, int line, int pos) {
    this.name = name;
    this.fileName = fileName;
    this.line = line;
    this.pos = pos;
    this.increment = increment;
  }

  @Override
  public void eval() {
    Variable variable = VariableManager.getCurrentFile().getVariable(name);

    if (variable == null) {
      new RuntimeError("Variable " + name + " is not defined", fileName, line, pos);
    }

    assert variable != null;
    Value value = variable.getValue();

    if (VariableManager.getCurrentFile().isFinal(name)) {
      new RuntimeError("Variable " + name + " is final", fileName, line, pos);
    }

    if (value.getType() != TokenType.INT_VALUE && value.getType() != TokenType.FLOAT_VALUE) {
      new RuntimeError("Variable " + name + " is not a number", fileName, line, pos);
    }

    boolean isFloat = value.getType() == TokenType.FLOAT_VALUE;

    if (increment == TokenType.PLUS) {
      VariableManager.getCurrentFile()
          .setVariable(
              name,
              isFloat ? new FloatValue(value.toFloat() + 1) : new IntValue(value.toInt() + 1),
              false);
    } else {
      VariableManager.getCurrentFile()
          .setVariable(
              name,
              isFloat ? new FloatValue(value.toFloat() - 1) : new IntValue(value.toInt() - 1),
              false);
    }
  }
}
