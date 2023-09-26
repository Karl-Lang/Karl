package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.values.*;

/** Represents a value expression in Karl. */
public class ValueExpression extends Expression {
  private final Value value;
  private final TokenType type;

  /**
   * Constructs a new ValueExpression object with an int value and type.
   *
   * @param value The value.
   * @param type The type.
   */
  public ValueExpression(Integer value, TokenType type) {
    this.value = new IntValue(value);
    this.type = type;
  }

  /**
   * Constructs a new ValueExpression object with a string value and type.
   *
   * @param value The value.
   * @param type The type.
   */
  public ValueExpression(String value, TokenType type) {
    if (type == TokenType.STR_VALUE) {
      this.value = new StringValue(value);
    } else {
      this.value = new NullValue(value);
    }
    this.type = type;
  }

  /**
   * Constructs a new ValueExpression object with a boolean value and type.
   *
   * @param value The value.
   * @param type The type.
   */
  public ValueExpression(Boolean value, TokenType type) {
    this.value = new BooleanValue(value);
    this.type = type;
  }

  /**
   * Constructs a new ValueExpression object with a float value and type.
   *
   * @param value The value.
   * @param type The type.
   */
  public ValueExpression(Float value, TokenType type) {
    this.value = new FloatValue(value);
    this.type = type;
  }

  /**
   * Constructs a new ValueExpression object with a char value and type.
   *
   * @param value The value.
   * @param type The type.
   */
  public ValueExpression(Character value, TokenType type) {
    this.value = new CharValue(value);
    this.type = type;
  }

  /**
   * Constructs a new ValueExpression object with a null value and type.
   *
   * @return The result.
   */
  @Override
  public Value eval() {
    return value;
  }
}
