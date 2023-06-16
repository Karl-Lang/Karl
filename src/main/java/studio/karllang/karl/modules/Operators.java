package studio.karllang.karl.modules;

import studio.karllang.karl.parser.TokenType;

import java.util.HashMap;

public class Operators {
    private static final HashMap<String, TokenType> operators = new HashMap<>();

    static {
        operators.put("+", TokenType.PLUS);
        operators.put("-", TokenType.MINUS);
        operators.put("*", TokenType.MULTIPLY);
        operators.put("/", TokenType.DIVIDE);
        operators.put("%", TokenType.MODULO);
    }

    public static TokenType getOperator(String name) {
        return operators.get(name);
    }

    public static boolean isOperator(TokenType type) {
        return operators.containsValue(type);
    }
}
