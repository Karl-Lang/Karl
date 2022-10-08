package fr.aiko.Ryoko.parser;// Create enum token type

public enum TokenType {
    // Keywords
    FUNC("func"),
    RETURN("return"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    FOR("for"),
    PRINT("System.print"),
    // Types
    INT("int"),
    FLOAT("float"),
    STRING("string"),
    BOOL("bool"),
    // Operators
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),
    EQUALS("="),
    // Brackets
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),
    LEFT_BRACKET("["),
    RIGHT_BRACKET("]"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    // Separators
    COMMA(","),
    COLON(":"),
    LT("<"),
    GT(">"),
    AMP("&"),
    BAR("|"),
    POINT("."),
    POW("^"),
    TILDE("~"),
    QUESTION("?"),
    EXCLAMATION("!"),
    SEMICOLON(";"),
    // Others
    IDENTIFIER("identifier"),
    EOF("EOF");

    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}