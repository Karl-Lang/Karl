package fr.aiko.Karl.std;

import fr.aiko.Karl.parser.ast.values.Value;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class VariableManager {
    private static final ConcurrentHashMap<String, Value> variables = new ConcurrentHashMap<>();
    private static final Object lock = new Object();

    public static Value getVariable(String name) {
        synchronized (lock) {
            return variables.get(name);
        }
    }

    public static void setVariable(String name, Value value) {
        synchronized (lock) {
            variables.put(name, value);
        }
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
