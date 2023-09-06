package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

public class CharValue extends Value {
  private final char value;

  public CharValue(char value) {
    this.value = value;
  }

  public char getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public int toInt() {
    return Integer.parseInt(toString());
  }

  @Override
  public float toFloat() {
    return Float.parseFloat(toString());
  }

  @Override
  public TokenType getType() {
    return TokenType.CHAR_VALUE;
  }
}
