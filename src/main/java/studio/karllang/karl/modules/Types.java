package studio.karllang.karl.modules;

import java.util.HashMap;
import studio.karllang.karl.parser.TokenType;

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

  public static TokenType getType(String name) {
    return types.get(name);
  }

  public static boolean isType(TokenType type) {
    return types.containsValue(type);
  }

  public static boolean contains(TokenType type) {
    return types.containsValue(type);
  }

  public static TokenType getValueType(String name) {
    return values_types.get(name);
  }

  public static boolean isValueType(TokenType type) {
    return values_types.containsValue(type);
  }

  public static boolean containsValue(TokenType type) {
    return values_types.containsValue(type);
  }
}
