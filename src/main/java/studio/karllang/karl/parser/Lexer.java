package studio.karllang.karl.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import studio.karllang.karl.errors.Error;
import studio.karllang.karl.errors.SyntaxError.SyntaxError;

public class Lexer {

  public final ArrayList<Token> tokens = new ArrayList<>();
  private final StringBuilder buffer;
  private final String OPERATOR_CHARS = "()[]{}^*=<>,!~&:+|./%?;-";
  private final Map<String, TokenType> OPERATORS = new HashMap<>();
  private final Map<String, TokenType> KEYWORDS = new HashMap<>();
  private final String fileName;
  public String input;
  private int position;
  private int line;

  public Lexer(String input, String fileName) {
    this.fileName = fileName;
    buffer = new StringBuilder(input);
    this.input = input;
    position = 0;
    line = 1;

    OPERATORS.put("+", TokenType.PLUS);
    OPERATORS.put("++", TokenType.PLUSPLUS);
    OPERATORS.put("--", TokenType.MINUSMINUS);
    OPERATORS.put("&&", TokenType.AND);
    OPERATORS.put("||", TokenType.OR);
    OPERATORS.put("==", TokenType.EQUALEQUAL);
    OPERATORS.put("!=", TokenType.NOT_EQUAL);
    OPERATORS.put(">", TokenType.GREATER);
    OPERATORS.put("<", TokenType.LESS);
    OPERATORS.put(">=", TokenType.GREATER_EQUAL);
    OPERATORS.put("<=", TokenType.LESS_EQUAL);
    OPERATORS.put("-", TokenType.MINUS);
    OPERATORS.put("*", TokenType.MULTIPLY);
    OPERATORS.put("/", TokenType.DIVIDE);
    OPERATORS.put("%", TokenType.MODULO);
    OPERATORS.put("=", TokenType.EQUAL);
    OPERATORS.put("(", TokenType.LEFT_PARENTHESIS);
    OPERATORS.put(")", TokenType.RIGHT_PARENTHESIS);
    OPERATORS.put("[", TokenType.LEFT_BRACKET);
    OPERATORS.put("]", TokenType.RIGHT_BRACKET);
    OPERATORS.put("{", TokenType.LEFT_BRACE);
    OPERATORS.put("}", TokenType.RIGHT_BRACE);
    OPERATORS.put(",", TokenType.COMMA);
    OPERATORS.put(":", TokenType.COLON);
    OPERATORS.put("&", TokenType.AMP);
    OPERATORS.put("|", TokenType.BAR);
    OPERATORS.put(".", TokenType.POINT);
    OPERATORS.put("^", TokenType.POW);
    OPERATORS.put("~", TokenType.TILDE);
    OPERATORS.put("?", TokenType.QUESTION);
    OPERATORS.put("!", TokenType.EXCLAMATION);
    OPERATORS.put(";", TokenType.SEMICOLON);
    OPERATORS.put("//", TokenType.COMMENTARY);

    KEYWORDS.put("func", TokenType.FUNC);
    KEYWORDS.put("return", TokenType.RETURN);
    KEYWORDS.put("if", TokenType.IF);
    KEYWORDS.put("else", TokenType.ELSE);
    KEYWORDS.put("while", TokenType.WHILE);
    KEYWORDS.put("for", TokenType.FOR);
    KEYWORDS.put("bool", TokenType.BOOL);
    KEYWORDS.put("final", TokenType.FINAL);
    KEYWORDS.put("int", TokenType.INT);
    KEYWORDS.put("float", TokenType.FLOAT);
    KEYWORDS.put("string", TokenType.STRING);
    KEYWORDS.put("char", TokenType.CHAR);
    KEYWORDS.put("void", TokenType.VOID);
    KEYWORDS.put("null", TokenType.NULL);

    tokenize();
  }

  public void tokenize() {
    if (input.length() == 0) {
      new Error("RetardError :)", "Empty file", fileName, line, 0);
    }

    while (position < input.length()) {
      final char c = input.charAt(position);
      if (c == '/' && position + 1 < input.length() && input.charAt(position + 1) == '/')
        tokenizeComment();
      else if (c == '/' && position + 1 < input.length() && input.charAt(position + 1) == '*')
        tokenizeMultiLineComment();
      else if (c == '\n' || c == '\r') {
        line++;
        position++;
      } else if (Character.isDigit(c)
          || (c == '-'
              && position + 1 < input.length()
              && Character.isDigit(input.charAt(position + 1)))) tokenizeNumber();
      else if (String.valueOf(c).matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$")) tokenizeIdentifier();
      else if (c == '"') tokenizeString();
      else if (c == '\'') tokenizeChar();
      else if (OPERATOR_CHARS.indexOf(c) != -1) tokenizeOperator();
      else if (Character.isWhitespace(c)) nextChar();
      else new SyntaxError("Unexpected character: " + c, fileName, line, position);
    }

    tokens.add(new Token(TokenType.EOF, "EOF", input.length(), line));
  }

  private void tokenizeComment() {
    while (position < input.length() && input.charAt(position) != '\n') {
      position++;
    }
  }

  private void tokenizeMultiLineComment() {
    while (position + 1 < input.length()
        && !(input.charAt(position) == '*' && input.charAt(position + 1) == '/')) {
      position++;
    }
    position += 2;
  }

  private void tokenizeChar() {
    nextChar();
    final char c = input.charAt(position);
    nextChar();
    if (input.charAt(position) != '\'') {
      if (input.charAt(position) != '\'') {
        new SyntaxError("Character type can only contain one character", fileName, line, position);
      } else {
        new SyntaxError("Expected ' at end of char value", fileName, line, position);
      }
    }
    nextChar();
    tokens.add(new Token(TokenType.CHAR_VALUE, String.valueOf(c), position - 2, line));
  }

  public void tokenizeNumber() {
    buffer.setLength(0);
    char c = input.charAt(position);
    if (position + 1 < input.length() && Character.isLetter(input.charAt(position + 1))) {
      new SyntaxError("Unexpected character: " + input.charAt(position), fileName, line, position);
    }

    while (true) {
      if (c == '\0') {
        break;
      }

      if ((c == '.' && buffer.indexOf(".") != -1) || (c == '-' && buffer.indexOf("-") != -1)) {
        new SyntaxError("Invalid number", fileName, line, position);
      } else if (!Character.isDigit(c) && (c != '.' && c != '-')) {
        break;
      }

      buffer.append(c);
      c = nextChar();
    }

    if (buffer.indexOf(".") != -1) {
      addToken(TokenType.FLOAT_VALUE, buffer.toString());
    } else {
      addToken(TokenType.INT_VALUE, buffer.toString());
    }
  }

  public void tokenizeIdentifier() {
    buffer.setLength(0);
    char c = input.charAt(position);
    while (true) {
      if (c == '\0') {
        break;
      }

      if (!Character.isLetterOrDigit(c) && c != '_') {
        break;
      }

      buffer.append(c);
      c = nextChar();
    }
    if (buffer.toString().equals("true") || buffer.toString().equals("false")) {
      addToken(TokenType.BOOL_VALUE, buffer.toString());
    } else if (buffer.toString().equals("show")) {
      addToken(TokenType.SHOW, buffer.toString());
    } else {
      addToken(KEYWORDS.getOrDefault(buffer.toString(), TokenType.IDENTIFIER), buffer.toString());
    }
  }

  public void tokenizeString() {
    buffer.setLength(0);
    char c = nextChar();
    while (true) {
      if (c == '\\') {
        c = nextChar();
        switch (c) {
          case 'n' -> buffer.append('\n');
          case 't' -> buffer.append('\t');
          case 'r' -> buffer.append('\r');
          case 'b' -> buffer.append('\b');
          case 'f' -> buffer.append('\f');
          case '\'' -> buffer.append('\'');
          case '"' -> buffer.append('\"');
          case '\\' -> buffer.append('\\');
          case '0' -> buffer.append('\0');
          default -> new SyntaxError("Invalid escape character: " + c, fileName, line, position);
        }
        c = nextChar();
      }

      if (c == '\0') {
        new SyntaxError("Unterminated string", fileName, line, position);
      }

      if (c == '"') {
        nextChar();
        break;
      }

      buffer.append(c);
      c = nextChar();
    }

    addToken(TokenType.STR_VALUE, buffer.toString());
  }

  public void tokenizeOperator() {
    buffer.setLength(0);
    char c = input.charAt(position);
    while (true) {
      if (c == '\0') {
        break;
      }

      if (OPERATOR_CHARS.indexOf(c) == -1) {
        break;
      }

      buffer.append(c);
      c = nextChar();

      if (OPERATORS.containsKey(buffer.toString())) {
        if (Character.toString(c).equals(buffer.toString())
            && Arrays.asList(new Character[] {'|', '&', '=', '+', '-', '/'}).contains(c)) {
          addToken(OPERATORS.get(buffer.toString() + c), buffer.toString() + c);
          nextChar();
        } else if (Arrays.asList(new String[] {">", "<", "!"}).contains(buffer.toString())
            && c == '=') {
          addToken(OPERATORS.get(buffer.toString() + c), buffer.toString() + c);
          nextChar();
        } else {
          addToken(OPERATORS.get(buffer.toString()), buffer.toString());
        }
        return;
      }
    }
  }

  public char nextChar() {
    position++;
    if (position >= input.length()) {
      return '\0';
    }
    return input.charAt(position);
  }

  public void addToken(TokenType type, String value) {
    tokens.add(new Token(type, value, position, line));
  }
}
