package fr.aiko.Karl.std;

import fr.aiko.Karl.parser.ast.values.Value;

import java.util.HashMap;

public final class VariableManager {
    private static final HashMap<String, Value> variables = new HashMap<>();

    public static Value getVariable(String name) {
        return variables.get(name);
    }

    public static void setVariable(String name, Value value) {
        variables.put(name, value);
    }

    public static void removeVariable(String name) {
        variables.remove(name);
    }

    public static boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    public static void clearVariables() {
        variables.clear();
    }
}
