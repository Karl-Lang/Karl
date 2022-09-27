package fr.aiko.Ryoko.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

    public final ArrayList<Token> tokens = new ArrayList<>();
    private final StringBuilder buffer;
    private int position;
    public String input;

    private final String OPERATOR_CHARS = "()[]{}^*=<>,!~&:+|./%?;-";
    private final Map<String, TokenType> OPERATORS = new HashMap<>();
    private final Map<String, TokenType> KEYWORDS = new HashMap<>();

    public Lexer(String input) {
        buffer = new StringBuilder(input);
        this.input = input.replaceAll("\\r\\n", "");;
        position = 0;

        OPERATORS.put("+", TokenType.PLUS);
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
        OPERATORS.put("<", TokenType.LT);
        OPERATORS.put(">", TokenType.GT);
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
        KEYWORDS.put("int", TokenType.INT);
        KEYWORDS.put("float", TokenType.FLOAT);
        KEYWORDS.put("string", TokenType.STRING);
        KEYWORDS.put("bool", TokenType.BOOL);

        tokenize();
    }

    public void tokenize() {

        if (input.length() == 0) {
            throw new RuntimeException("Empty file");
        }

        while (position < input.length()) {
            final char c = input.charAt(position);
            if (Character.isDigit(c)) tokenizeNumber();
            else if (Character.isLetter(c)) tokenizeIdentifier();
            else if (c == '"') tokenizeString();
            else if (OPERATOR_CHARS.indexOf(c) != -1) tokenizeOperator();
            else if (Character.isWhitespace(c)) nextChar();
            else throw new RuntimeException("Unexpected character: " + c);
        }
    }

    public void tokenizeNumber() {
        buffer.setLength(0);
        char c = input.charAt(position);
        while (true) {
            if (c == '\0') {
                break;
            }

            if (c == '.' && buffer.indexOf(".") != -1) {
                throw new RuntimeException("Invalid number");
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

        addToken(KEYWORDS.getOrDefault(buffer.toString(), TokenType.IDENTIFIER), buffer.toString());
    }

    public void tokenizeString() {
        buffer.setLength(0);
        char c = nextChar();
        while (true) {
            if (c == '\0') {
                throw new RuntimeException("Unterminated string");
            }

            if (c == '"') {
                nextChar();
                break;
            }

            buffer.append(c);
            c = nextChar();
            System.out.println(c);
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
        tokens.add(new Token(type, value, position));
    }
}