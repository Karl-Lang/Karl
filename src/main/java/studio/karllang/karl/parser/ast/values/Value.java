package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

/** Represents a value in Karl. */
public abstract class Value {

  /**
   * Returns a string representation of the value.
   *
   * @return The string representation of the value.
   */
  public abstract String toString();

  /**
   * Returns the value as an integer.
   *
   * @return The integer representation of the value.
   */
  public abstract int toInt();

  /**
   * Returns the value as a float.
   *
   * @return The float representation of the value.
   */
  public abstract float toFloat();

  /**
   * Returns the TokenType associated with this value.
   *
   * @return The TokenType representing a value.
   */
  public abstract TokenType getType();
}
