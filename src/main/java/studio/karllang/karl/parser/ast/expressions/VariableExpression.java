package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.modules.File;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a variable expression in Karl. */
public class VariableExpression extends Expression {
  public final Value value;
  private final String name;
  private final boolean isFinal;
  private final File file;
  private final boolean isDeclaration;
  private final int line;
  private final int pos;

  /**
   * Constructs a new VariableExpression object with the specified name, value, isFinal, file,
   * isDeclaration, line and position.
   *
   * @param name The name.
   * @param value The value.
   * @param isFinal If the variable is final.
   * @param file The file.
   * @param isDeclaration If the variable is a declaration.
   * @param line The line number.
   * @param pos The position.
   */
  public VariableExpression(
      String name,
      Value value,
      boolean isFinal,
      File file,
      boolean isDeclaration,
      int line,
      int pos) {
    this.name = name;
    this.value = value;
    this.isFinal = isFinal;
    this.file = file;
    this.isDeclaration = isDeclaration;
    this.line = line;
    this.pos = pos;
  }

  /**
   * Evaluates the variable expression.
   *
   * @return The result.
   */
  @Override
  public Value eval() {
    setValue(value);
    return getValue();
  }

  /**
   * Returns the name.
   *
   * @return The name.
   */
  public synchronized Value getValue() {
    return this.file.getVariableManager().getVariable(name).getValue();
  }

  /**
   * Sets the value.
   *
   * @param value The value.
   */
  public synchronized void setValue(Value value) {
    this.file.getVariableManager().setVariable(name, value, isFinal, isDeclaration, line, pos);
  }
}
