package studio.karllang.karl.parser; // Create enum token type

public enum TokenType {
  EQUALEQUAL("=="),
  NOT_EQUAL("!="),
  GREATER(">"),
  LESS("<"),
  GREATER_EQUAL(">="),
  LESS_EQUAL("<="),
  FUNC("func"),
  RETURN("return"),
  IF("if"),
  ELSE("else"),
  WHILE("while"),
  FOR("for"),
  SHOW("System.show"),
  INT("int"),
  FLOAT("float"),
  FLOAT_VALUE("fltval"),
  INT_VALUE("intval"),
  BOOL_VALUE("boolval"),
  STRING("string"),
  STR_VALUE("strvalue"),
  BOOL("bool"),
  PLUS("+"),
  MINUS("-"),
  MULTIPLY("*"),
  DIVIDE("/"),
  MODULO("%"),
  EQUAL("="),
  LEFT_PARENTHESIS("("),
  RIGHT_PARENTHESIS(")"),
  LEFT_BRACKET("["),
  RIGHT_BRACKET("]"),
  LEFT_BRACE("{"),
  RIGHT_BRACE("}"),
  COMMA(","),
  COLON(":"),
  AMP("&"),
  BAR("|"),
  POINT("."),
  POW("^"),
  TILDE("~"),
  QUESTION("?"),
  EXCLAMATION("!"),
  SEMICOLON(";"),
  IDENTIFIER("identifier"),
  FINAL("final"),
  EOF("EOF"),
  AND("&&"),
  OR("||"),
  CHAR("char"),
  MINUSMINUS("--"),
  PLUSPLUS("++"),
  COMMENTARY("//"),
  VOID("void"),
  NULL("null"),
  CHAR_VALUE("charVal");

  private final String name;

  TokenType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
