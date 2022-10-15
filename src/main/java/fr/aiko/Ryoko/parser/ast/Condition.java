package fr.aiko.Ryoko.parser.ast;

import fr.aiko.Ryoko.parser.TokenType;

public class Condition {
    private final String left;
    private final String right;
    private final TokenType operator;

    public Condition(String left, String right, TokenType operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public TokenType getOperator() {
        return operator;
    }

    public boolean execute() {
        switch (operator) {
            case EQUAL:
                return left.equals(right);
            case NOT_EQUAL:
                return !left.equals(right);
            case GREATER:
                return Integer.parseInt(left) > Integer.parseInt(right);
            case LESS:
                return Integer.parseInt(left) < Integer.parseInt(right);
            case GREATER_EQUAL:
                return Integer.parseInt(left) >= Integer.parseInt(right);
            case LESS_EQUAL:
                return Integer.parseInt(left) <= Integer.parseInt(right);
            default:
                throw new RuntimeException("Unknown operator " + operator);
        }
    }
}
