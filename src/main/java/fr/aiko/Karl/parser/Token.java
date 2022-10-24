package fr.aiko.Karl.parser;

public class Token {
    private final int position;
    private final int line;
    private final TokenType type;
    private String value;

    public Token(TokenType type, String value, int position, int line) {
        this.type = type;
        this.value = value;
        this.position = position;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String s) {
        value = s;
    }

    public int getPosition() {
        return position;
    }

    public int getLine() {
        return line;
    }
}
