package studio.karllang.karl.modules;

import java.util.HashMap;
import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a logical operator in Karl. */
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

  /**
   * Evaluates an and operator.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The result.
   */
  public static boolean and(boolean a, boolean b) {
    return a && b;
  }

  /**
   * Evaluates an or operator.
   *
   * @param a The first value.
   * @param b The second value.
   * @return The result.
   */
  public static boolean or(boolean a, boolean b) {
    return a || b;
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

  /**
   * Compares two values with the specified operator.
   *
   * @param firstNumber The first value.
   * @param secondNumber The second value.
   * @param operator The operator.
   * @param file The file.
   * @param line The line number.
   * @param pos The position.
   * @return The result.
   */
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
