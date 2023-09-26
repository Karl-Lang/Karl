package studio.karllang.karl.parser;

/**
 * Represents a token in a lexical analysis process. Tokens are used to identify and categorize
 * components of source code.
 */
public class Token {
  private final int position; // The position of the token in the source code.
  private final int line; // The line number in which the token appears.
  private final TokenType type; // The type of the token.
  private String value; // The value associated with the token, if applicable.

  /**
   * Constructs a new Token object with the specified properties.
   *
   * @param type The type of the token.
   * @param value The value associated with the token, if applicable.
   * @param position The position of the token in the source code.
   * @param line The line number in which the token appears.
   */
  public Token(TokenType type, String value, int position, int line) {
    this.type = type;
    this.value = value;
    this.position = position;
    this.line = line;
  }

  /**
   * Retrieves the type of the token.
   *
   * @return The TokenType of the token.
   */
  public TokenType getType() {
    return type;
  }

  /**
   * Retrieves the value associated with the token, if applicable.
   *
   * @return The value of the token, which may be null.
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value associated with the token.
   *
   * @param s The new value to associate with the token.
   */
  public void setValue(String s) {
    value = s;
  }

  /**
   * Retrieves the position of the token in the source code.
   *
   * @return The position of the token.
   */
  public int getPosition() {
    return position;
  }

  /**
   * Retrieves the line number in which the token appears.
   *
   * @return The line number of the token.
   */
  public int getLine() {
    return line;
  }
}
