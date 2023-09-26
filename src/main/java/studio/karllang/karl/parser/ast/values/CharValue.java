package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

/** Represents a character value in a programming language. */
public class CharValue extends Value {
  private final char value;

  /**
   * Constructs a new CharValue object with the specified character value.
   *
   * @param value The character value.
   */
  public CharValue(char value) {
    this.value = value;
  }

  /**
   * Returns a string representation of the character value.
   *
   * @return The string representation of the character value.
   */
  public char getValue() {
    return value;
  }

  /**
   * Returns the character value as an integer.
   *
   * @return The integer representation of the character value.
   */
  @Override
  public String toString() {
    return String.valueOf(value);
  }

  /**
   * Returns the character value as an integer.
   *
   * @return The integer representation of the character value.
   */
  @Override
  public int toInt() {
    return Integer.parseInt(toString());
  }

  /**
   * Returns the character value as a float.
   *
   * @return The float representation of the character value.
   */
  @Override
  public float toFloat() {
    return Float.parseFloat(toString());
  }

  /**
   * Returns the TokenType associated with this value, which is 'CHAR_VALUE'.
   *
   * @return The TokenType representing a character value.
   */
  @Override
  public TokenType getType() {
    return TokenType.CHAR_VALUE;
  }
}
