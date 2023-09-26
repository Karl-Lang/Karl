package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a variable call expression in Karl. */
public class VariableCallExpression extends Expression {
  private final String name;
  private final String fileName;
  private final File file;
  private final int line;
  private final int pos;

  /**
   * Constructs a new VariableCallExpression object with the specified name, file, line and
   * position.
   *
   * @param name The name.
   * @param file The file.
   * @param line The line number.
   * @param pos The position.
   */
  public VariableCallExpression(String name, File file, int line, int pos) {
    this.name = name;
    this.file = file;
    this.fileName = file.getName();
    this.line = line;
    this.pos = pos;
  }

  /**
   * Returns the name.
   *
   * @return The name.
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * Evaluates the variable call expression.
   *
   * @return The result.
   */
  @Override
  public Value eval() {
    if (this.file.getVariableManager().getVariable(name) == null) {
      new RuntimeError("Variable " + name + " is not defined", file.getStringPath(), line, pos);
    }

    return this.file.getVariableManager().getVariable(name).getValue();
  }
}
