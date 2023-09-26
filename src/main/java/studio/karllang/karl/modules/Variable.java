package studio.karllang.karl.modules;

import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a variable in Karl. */
public class Variable {
  private final TokenType type;
  private final String name;
  private final boolean isFinal;
  private final boolean isDeclaration;
  private Value value;

  /**
   * Constructs a new Variable object with the specified type, name, value, isFinal and
   * isDeclaration.
   *
   * @param type The type.
   * @param name The name.
   * @param value The value.
   * @param isFinal If the variable is final.
   * @param isDeclaration If the variable is a declaration.
   */
  public Variable(
      TokenType type, String name, Value value, boolean isFinal, boolean isDeclaration) {
    this.type = type;
    this.name = name;
    this.value = value;
    this.isFinal = isFinal;
    this.isDeclaration = isDeclaration;
  }

  /**
   * Returns the type.
   *
   * @return The type.
   */
  public TokenType getType() {
    return type;
  }

  /**
   * Returns the name.
   *
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value.
   *
   * @return The value.
   */
  public Value getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value The value.
   */
  public void setValue(Value value) {
    this.value = value;
  }

  /**
   * Returns if the variable is a declaration.
   *
   * @return If the variable is a declaration.
   */
  public boolean isDeclaration() {
    return isDeclaration;
  }

  /**
   * Returns if the variable is final.
   *
   * @return True if the variable is final.
   */
  public boolean isFinal() {
    return isFinal;
  }
}
