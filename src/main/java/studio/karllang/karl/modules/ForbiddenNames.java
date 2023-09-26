package studio.karllang.karl.modules;

import java.util.Arrays;

public final class ForbiddenNames {
  private static final String[] FORBIDDEN_VARIABLE_NAMES = {
    "string",
    "int",
    "float",
    "double",
    "bool",
    "char",
    "void",
    "null",
    "true",
    "false",
    "if",
    "else",
    "while",
    "for",
    "do",
    "switch",
    "case",
    "default",
    "break",
    "continue",
    "return",
    "new",
    "this",
    "super",
    "class",
    "interface",
    "package",
    "import",
    "public",
    "private",
    "protected",
    "static",
    "final",
    "abstract",
    "native",
    "synchronized",
    "transient",
    "volatile",
    "strictfp",
    "extends",
    "implements",
    "instanceof",
    "try",
    "catch",
    "finally",
    "throw",
    "throws",
    "assert",
    "enum",
    "goto",
    "const",
    "byte",
    "short",
    "long",
    "true",
    "false",
    "null",
    "void",
    "eval"
  };

  private static final String[] FORBIDDEN_FUNCTION_NAMES = {"show", "eval"};

  /**
   * Checks if the name is forbidden.
   *
   * @param name The name.
   * @return If the name is forbidden.
   */
  public static boolean isForbiddenName(String name) {
    if (Arrays.asList(FORBIDDEN_VARIABLE_NAMES).contains(name)) return true;
    return Arrays.asList(FORBIDDEN_FUNCTION_NAMES).contains(name);
  }
}
