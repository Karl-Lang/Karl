package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

/** Represents a null value in a Karl. */
public class NullValue extends Value {
  public final String value;

  /**
   * Constructs a new NullValue object with the specified null value.
   *
   * @param value The null value.
   */
  public NullValue(String value) {
    this.value = value;
  }

  /**
   * Returns a string representation of the null value.
   *
   * @return The string representation of the null value.
   */
  @Override
  public String toString() {
    return value;
  }

  /**
   * Returns the null value as an integer.
   *
   * @return The integer representation of the null value.
   */
  @Override
  public int toInt() {
    return 0;
  }

  /**
   * Returns the null value as a float.
   *
   * @return The float representation of the null value.
   */
  @Override
  public float toFloat() {
    return 0;
  }

  /**
   * Returns the TokenType associated with this value, which is 'NULL'.
   *
   * @return The TokenType representing a null value.
   */
  @Override
  public TokenType getType() {
    return TokenType.NULL;
  }
}
