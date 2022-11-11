package fr.aiko.Karl.std;

import fr.aiko.Karl.parser.TokenType;

import java.util.HashMap;

public class Operators {
    private static final HashMap<String, TokenType> operators = new HashMap<>();
    static {
        operators.put("+", TokenType.PLUS);
        operators.put("-", TokenType.MINUS);
        operators.put("*", TokenType.MULTIPLY);
        operators.put("/", TokenType.DIVIDE);
        operators.put("%", TokenType.MODULO);
        operators.put("=", TokenType.EQUAL);
        operators.put("==", TokenType.EQUALEQUAL);
        operators.put("!=", TokenType.NOT_EQUAL);
        operators.put(">", TokenType.GREATER);
        operators.put("<", TokenType.LESS);
        operators.put(">=", TokenType.GREATER_EQUAL);
        operators.put("<=", TokenType.LESS_EQUAL);
        operators.put("&&", TokenType.AND);
        operators.put("||", TokenType.OR);
        operators.put("!", TokenType.EXCLAMATION);
    }

    public static TokenType getType(String name) {
        return operators.get(name);
    }

    public static boolean isType(TokenType type) {
        return operators.containsValue(type);
    }
}
