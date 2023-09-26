package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

/** Represents a boolean value in a programming language. Extends the abstract class 'Value'. */
public class BooleanValue extends Value {
  private final Boolean value; // The boolean value.

  /**
   * Constructs a new BooleanValue object with the specified boolean value.
   *
   * @param value The boolean value.
   */
  public BooleanValue(Boolean value) {
    this.value = value;
  }

  /**
   * Returns a string representation of the boolean value.
   *
   * @return The string representation of the boolean value.
   */
  @Override
  public String toString() {
    return value.toString();
  }

  /**
   * Returns the boolean value as an integer.
   *
   * @return The integer representation of the boolean value.
   */
  @Override
  public int toInt() {
    return 0;
  }

  /**
   * Returns the boolean value as a float.
   *
   * @return The float representation of the boolean value.
   */
  @Override
  public float toFloat() {
    return 0;
  }

  /**
   * Returns the TokenType associated with this value, which is 'BOOL_VALUE'.
   *
   * @return The TokenType representing a boolean value.
   */
  @Override
  public TokenType getType() {
    return TokenType.BOOL_VALUE;
  }
}
