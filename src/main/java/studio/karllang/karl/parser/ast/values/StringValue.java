package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

/** Represents a string value in a programming language. */
public class StringValue extends Value {
  private final String value;

  /**
   * Constructs a new StringValue object with the specified string value.
   *
   * @param value The string value.
   */
  public StringValue(String value) {
    this.value = value;
  }

  /**
   * Returns a string representation of the string value.
   *
   * @return The string representation of the string value.
   */
  @Override
  public String toString() {
    return value;
  }

  /**
   * Returns the string value as an integer.
   *
   * @return The integer representation of the string value.
   */
  @Override
  public int toInt() {
    return Integer.parseInt(value);
  }

  /**
   * Returns the string value as a float.
   *
   * @return The float representation of the string value.
   */
  @Override
  public float toFloat() {
    return Float.parseFloat(value);
  }

  /**
   * Returns the TokenType associated with this value, which is 'STR_VALUE'.
   *
   * @return The TokenType representing a string value.
   */
  @Override
  public TokenType getType() {
    return TokenType.STR_VALUE;
  }
}
