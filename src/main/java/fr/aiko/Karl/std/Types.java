package fr.aiko.Karl.std;

import fr.aiko.Karl.parser.TokenType;

import java.util.HashMap;

public final class Types {
    private static final HashMap<String, TokenType> types = new HashMap<>();

    static {
        types.put("int", TokenType.INT);
        types.put("string", TokenType.STRING);
        types.put("bool", TokenType.BOOL);
        types.put("float", TokenType.FLOAT);
    }

    public static TokenType getType(String name) {
        return types.get(name);
    }

    public static boolean isType(TokenType type) {
        return types.containsValue(type);
    }

    public static boolean contains(TokenType type) {
        return types.containsValue(type);
    }
}
