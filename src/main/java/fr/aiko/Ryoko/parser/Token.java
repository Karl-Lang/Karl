package fr.aiko.Ryoko.parser;

public class Token {
    private final int startPosition;
    public TokenType type;
    public String value;

    public Token(TokenType type, String value, int startPosition) {
        this.type = type;
        this.value = value;
        this.startPosition = startPosition;
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

    public int getStart() {
        return startPosition;
    }
}
