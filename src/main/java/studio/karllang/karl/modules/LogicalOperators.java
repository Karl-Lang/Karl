package studio.karllang.karl.modules;

import java.util.HashMap;
import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.values.Value;

public class LogicalOperators {
  public static final HashMap<String, TokenType> operators = new HashMap<>();

  static {
    operators.put("&&", TokenType.AND);
    operators.put("||", TokenType.OR);
    operators.put("==", TokenType.EQUALEQUAL);
    operators.put("!=", TokenType.NOT_EQUAL);
    operators.put(">", TokenType.GREATER);
    operators.put("<", TokenType.LESS);
    operators.put(">=", TokenType.GREATER_EQUAL);
    operators.put("<=", TokenType.LESS_EQUAL);
  }

  public static boolean and(boolean a, boolean b) {
    return a && b;
  }

  public static boolean or(boolean a, boolean b) {
    return a || b;
  }

  public static boolean isOperator(TokenType type) {
    return operators.containsValue(type);
  }

  public static Boolean compare(
      Value firstNumber, Value secondNumber, TokenType operator, File file, int line, int pos) {
    if (firstNumber.getType() != TokenType.INT_VALUE
        && firstNumber.getType() != TokenType.FLOAT_VALUE
        && secondNumber.getType() != TokenType.INT_VALUE
        && secondNumber.getType() != TokenType.FLOAT_VALUE) {
      new RuntimeError(
          "Type mismatch : "
              + firstNumber.getType().toString().toLowerCase()
              + " and "
              + secondNumber.getType().toString().toLowerCase(),
          file.getStringPath(),
          line,
          pos);
      return null;
    }
    return switch (operator) {
      case LESS -> firstNumber.toFloat() < secondNumber.toFloat();
      case LESS_EQUAL -> firstNumber.toFloat() <= secondNumber.toFloat();
      case GREATER_EQUAL -> firstNumber.toFloat() >= secondNumber.toFloat();
      case GREATER -> firstNumber.toFloat() > secondNumber.toFloat();
      case EQUALEQUAL -> firstNumber.toFloat() == secondNumber.toFloat();
      case NOT_EQUAL -> firstNumber.toFloat() != secondNumber.toFloat();
      case OR, AND -> true;
      default -> throw new RuntimeException("Unknown operator: " + operator);
    };
  }
}
