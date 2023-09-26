package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

/** Represents a float value in a programming language. */
public class FloatValue extends Value {
  private final float value;

  /**
   * Constructs a new FloatValue object with the specified float value.
   *
   * @param value The float value.
   */
  public FloatValue(float value) {
    this.value = value;
  }

  /**
   * Returns a string representation of the float value.
   *
   * @return The string representation of the float value.
   */
  @Override
  public String toString() {
    return Float.toString(value);
  }

  /**
   * Returns the float value as an integer.
   *
   * @return The integer representation of the float value.
   */
  @Override
  public int toInt() {
    return Integer.parseInt(toString());
  }

  /**
   * Returns the float value as a float.
   *
   * @return The float representation of the float value.
   */
  @Override
  public float toFloat() {
    return value;
  }

  /**
   * Returns the TokenType associated with this value, which is 'FLOAT_VALUE'.
   *
   * @return The TokenType representing a float value.
   */
  @Override
  public TokenType getType() {
    return TokenType.FLOAT_VALUE;
  }
}
