package studio.karllang.karl.modules;

import java.util.HashMap;
import studio.karllang.karl.parser.TokenType;

/** All operators in Karl. */
public class Operators {
  private static final HashMap<String, TokenType> operators = new HashMap<>();

  static {
    operators.put("+", TokenType.PLUS);
    operators.put("-", TokenType.MINUS);
    operators.put("*", TokenType.MULTIPLY);
    operators.put("/", TokenType.DIVIDE);
    operators.put("%", TokenType.MODULO);
    operators.put("**", TokenType.POWER);
  }

  /**
   * Checks if the specified type is an operator.
   *
   * @param type The type.
   * @return True if the type is an operator, otherwise false.
   */
  public static boolean isOperator(TokenType type) {
    return operators.containsValue(type);
  }
}
