package fr.aiko.Karl.parser;

import fr.aiko.Karl.ErrorManager.Error;
import fr.aiko.Karl.ErrorManager.SyntaxError.SyntaxError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        OPERATORS.put("&&", TokenType.AND);
        OPERATORS.put("||", TokenType.OR);
        OPERATORS.put("==", TokenType.EQUAL);
        OPERATORS.put("!=", TokenType.NOT_EQUAL);
        OPERATORS.put(">", TokenType.GREATER);
        OPERATORS.put("<", TokenType.LESS);
        OPERATORS.put(">=", TokenType.GREATER_EQUAL);
        OPERATORS.put("<=", TokenType.LESS_EQUAL);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.MULTIPLY);
        OPERATORS.put("/", TokenType.DIVIDE);
        OPERATORS.put("%", TokenType.MODULO);
        OPERATORS.put("=", TokenType.EQUALS);
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


        KEYWORDS.put("func", TokenType.FUNC);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("bool", TokenType.BOOL);
        KEYWORDS.put("final", TokenType.FINAL);

        tokenize();
    }

    public void tokenize() {
        if (input.length() == 0) {
            new Error("RetardError", "Empty file", fileName, line);
        }

        while (position < input.length()) {
            final char c = input.charAt(position);
            if (c == '\n' || c == '\r') {
                line++;
                position++;
            } else if (Character.isDigit(c)) tokenizeNumber();
            else if (Character.isLetter(c)) tokenizeIdentifier();
            else if (c == '"') tokenizeString();
            else if (c == '\'') tokenizeChar();
            else if (OPERATOR_CHARS.indexOf(c) != -1) tokenizeOperator();
            else if (Character.isWhitespace(c)) nextChar();
            else new SyntaxError("Unexpected character: " + c, fileName, line);
        }

        tokens.add(new Token(TokenType.EOF, "EOF", input.length(), line));
    }

    private void tokenizeChar() {
        nextChar();
        final char c = input.charAt(position);
        nextChar();
        if (input.charAt(position) != '\'') {
            if (input.charAt(position) != '\'') {
                new SyntaxError("Character type can only contain one character", fileName, line);
            } else {
                new SyntaxError("Expected ' at end of char value", fileName, line);
            }
        }
        nextChar();
        tokens.add(new Token(TokenType.CHAR, String.valueOf(c), position - 2, line));
    }

    public void tokenizeNumber() {
        buffer.setLength(0);
        char c = input.charAt(position);
        if (Character.isLetter(input.charAt(position + 1))) {
            new SyntaxError("Unexpected character: " + input.charAt(position), fileName, line);
        }

        while (true) {
            if (c == '\0') {
                break;
            }

            if (c == '.' && buffer.indexOf(".") != -1) {
                new SyntaxError("Invalid number", fileName, line);
            } else if (!Character.isDigit(c)) {
                break;
            }

            buffer.append(c);
            c = nextChar();
        }

        if (buffer.indexOf(".") == 1) {
            addToken(TokenType.FLOAT, buffer.toString());
        } else {
            addToken(TokenType.INT, buffer.toString());
        }
    }

    public void tokenizeIdentifier() {
        buffer.setLength(0);
        char c = input.charAt(position);
        while (true) {
            if (c == '\0') {
                break;
            }

            if (!Character.isLetterOrDigit(c)) {
                break;
            }

            buffer.append(c);
            c = nextChar();
        }

        if (!buffer.toString().equals("true") && !buffer.toString().equals("false")) {
            addToken(KEYWORDS.getOrDefault(buffer.toString(), TokenType.IDENTIFIER), buffer.toString());
        } else {
            addToken(TokenType.BOOL, buffer.toString());
        }
    }

    public void tokenizeString() {
        buffer.setLength(0);
        char c = nextChar();
        while (true) {
            if (c == '\0') {
                new SyntaxError("Unterminated string", fileName, line);
            }

            if (c == '"') {
                nextChar();
                break;
            }

            buffer.append(c);
            c = nextChar();
        }

        addToken(TokenType.STRING, buffer.toString());
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
                addToken(OPERATORS.get(buffer.toString()), buffer.toString());
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