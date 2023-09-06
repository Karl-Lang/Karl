package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.VariableManager;

public class VariableCallExpression extends Expression {
  private final String name;
  private final String fileName;
  private final int line;
  private final int pos;

  public VariableCallExpression(String name, String fileName, int line, int pos) {
    this.name = name;
    this.fileName = fileName;
    this.line = line;
    this.pos = pos;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Value eval() {
    if (VariableManager.getCurrentFile().getVariable(name) == null) {
      new RuntimeError("Variable " + name + " is not defined", fileName, line, pos);
    }

    return VariableManager.getCurrentFile().getVariable(name).getValue();
  }
}
