package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

/** Represents an integer value in a programming language. */
public class IntValue extends Value {
  private final int value;

  /**
   * Constructs a new IntValue object with the specified integer value.
   *
   * @param value The integer value.
   */
  public IntValue(int value) {
    this.value = value;
  }

  /**
   * Returns a string representation of the integer value.
   *
   * @return The string representation of the integer value.
   */
  @Override
  public String toString() {
    return Integer.toString(value);
  }

  /**
   * Returns the integer value as an integer.
   *
   * @return The integer representation of the integer value.
   */
  @Override
  public int toInt() {
    return value;
  }

  /**
   * Returns the integer value as a float.
   *
   * @return The float representation of the integer value.
   */
  @Override
  public float toFloat() {
    return Float.parseFloat(toString());
  }

  /**
   * Returns the TokenType associated with this value, which is 'INT_VALUE'.
   *
   * @return The TokenType representing an integer value.
   */
  @Override
  public TokenType getType() {
    return TokenType.INT_VALUE;
  }
}
