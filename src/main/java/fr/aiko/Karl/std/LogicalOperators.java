package fr.aiko.Karl.std;

import fr.aiko.Karl.parser.TokenType;

import java.util.HashMap;

public class LogicalOperators {
    public static final HashMap<String, TokenType> operators = new HashMap<>();

    static {
        operators.put("&&", TokenType.AND);
        operators.put("||", TokenType.OR);
        operators.put("!", TokenType.EXCLAMATION);
        operators.put("==", TokenType.EQUALEQUAL);
        operators.put("!=", TokenType.NOT_EQUAL);
        operators.put(">", TokenType.GREATER);
        operators.put("<", TokenType.LESS);
        operators.put(">=", TokenType.GREATER_EQUAL);
        operators.put("<=", TokenType.LESS_EQUAL);
    }

    public static boolean and(boolean a, boolean b) {
        return a && b;
    }

    public static boolean or(boolean a, boolean b) {
        return a || b;
    }

    public static boolean isOperator(TokenType type) {
        return operators.containsValue(type);
    }

    public static Boolean compare(float toInt, float toInt1, TokenType operator) {
        return switch (operator) {
            case LESS -> toInt < toInt1;
            case LESS_EQUAL -> toInt <= toInt1;
            case GREATER_EQUAL -> toInt >= toInt1;
            case GREATER -> toInt > toInt1;
            case EQUALEQUAL -> toInt == toInt1;
            case NOT_EQUAL -> toInt != toInt1;
            default -> throw new RuntimeException("Unknown operator: " + operator);
        };
    }
}
