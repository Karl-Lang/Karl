package studio.karllang.karl.modules;

import java.util.HashMap;
import studio.karllang.karl.parser.TokenType;

/** All types in Karl. */
public final class Types {
  private static final HashMap<String, TokenType> types = new HashMap<>();
  private static final HashMap<String, TokenType> values_types = new HashMap<>();

  static {
    types.put("int", TokenType.INT);
    types.put("string", TokenType.STRING);
    types.put("bool", TokenType.BOOL);
    types.put("float", TokenType.FLOAT);
    types.put("char", TokenType.CHAR);
    types.put("null", TokenType.NULL);

    values_types.put("string", TokenType.STR_VALUE);
    values_types.put("int", TokenType.INT_VALUE);
    values_types.put("bool", TokenType.BOOL_VALUE);
    values_types.put("float", TokenType.FLOAT_VALUE);
    values_types.put("char", TokenType.CHAR_VALUE);
  }

  /**
   * Checks if the specified type is a type.
   *
   * @param expectedType The expected type.
   * @param type The type.
   * @return True if the specified type is the same as type, otherwise false.
   */
  public static boolean checkValueType(TokenType expectedType, TokenType type) {
    return switch (expectedType) {
      case INT -> type == TokenType.INT_VALUE;
      case FLOAT -> type == TokenType.FLOAT_VALUE;
      case STRING -> type == TokenType.STR_VALUE;
      case CHAR -> type == TokenType.CHAR_VALUE;
      case BOOL -> type == TokenType.BOOL_VALUE;
      default -> false;
    };
  }

  /**
   * Gets the name of the specified type.
   *
   * @param type The type.
   * @return The name.
   */
  public static String getTypeName(TokenType type) {
    return switch (type) {
      case INT_VALUE -> "int";
      case FLOAT_VALUE -> "float";
      case STR_VALUE -> "string";
      case CHAR_VALUE -> "char";
      case BOOL_VALUE -> "bool";
      default -> type.getName().toLowerCase();
    };
  }

  /**
   * Gets the type with the specified name.
   *
   * @param name The name.
   * @return The type.
   */
  public static TokenType getType(String name) {
    return types.get(name);
  }

  /**
   * Checks if the specified type is a type.
   *
   * @param type The type.
   * @return True if the type is a type, otherwise false.
   */
  public static boolean isType(TokenType type) {
    return types.containsValue(type);
  }

  /**
   * Checks if the types contains the specified type.
   *
   * @param type The type.
   * @return True if the types contains the specified type, otherwise false.
   */
  public static boolean contains(TokenType type) {
    return types.containsValue(type);
  }

  /**
   * Checks if the specified type is a value type.
   *
   * @param type The type.
   * @return True if the type is a value type, otherwise false.
   */
  public static boolean isValueType(TokenType type) {
    return values_types.containsValue(type);
  }
}
