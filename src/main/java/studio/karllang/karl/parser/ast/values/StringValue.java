package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

public class StringValue extends Value {
  private final String value;

  public StringValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public int toInt() {
    return Integer.parseInt(value);
  }

  @Override
  public float toFloat() {
    return Float.parseFloat(value);
  }

  @Override
  public TokenType getType() {
    return TokenType.STR_VALUE;
  }
}
