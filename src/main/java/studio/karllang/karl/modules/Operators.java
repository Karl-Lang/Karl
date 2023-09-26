package studio.karllang.karl.modules;

import java.util.HashMap;
import studio.karllang.karl.parser.TokenType;

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

  public static TokenType getOperator(String name) {
    return operators.get(name);
  }

  public static boolean isOperator(TokenType type) {
    return operators.containsValue(type);
  }
}
