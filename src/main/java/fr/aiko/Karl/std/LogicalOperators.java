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

    public static Boolean compare(float firstNumber, float secondNumber, TokenType operator) {
        return switch (operator) {
            case LESS -> firstNumber < secondNumber;
            case LESS_EQUAL -> firstNumber <= secondNumber;
            case GREATER_EQUAL -> firstNumber >= secondNumber;
            case GREATER -> firstNumber > secondNumber;
            case EQUALEQUAL -> firstNumber == secondNumber;
            case NOT_EQUAL -> firstNumber != secondNumber;
            default -> throw new RuntimeException("Unknown operator: " + operator);
        };
    }
}
