package studio.karllang.karl.parser.ast.values;

import studio.karllang.karl.parser.TokenType;

public class FloatValue extends Value {
  private final float value;

  public FloatValue(float value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return Float.toString(value);
  }

  @Override
  public int toInt() {
    return Integer.parseInt(toString());
  }

  @Override
  public float toFloat() {
    return value;
  }

  @Override
  public TokenType getType() {
    return TokenType.FLOAT_VALUE;
  }
}
