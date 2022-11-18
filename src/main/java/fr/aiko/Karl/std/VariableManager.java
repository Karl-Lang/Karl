package fr.aiko.Karl.std;

import fr.aiko.Karl.parser.ast.values.Value;

import java.util.HashMap;

public final class VariableManager {

    private static Scope currentScope = new Scope(null);

    public static Value getVariable(String name) {
        return currentScope.variables.get(name);
    }

    public static void setVariable(String name, Value value) {
        currentScope.variables.put(name, value);
    }

    public static void removeVariable(String name) {
        currentScope.variables.remove(name);
    }

    public static boolean containsVariable(String name) {
        return currentScope.variables.containsKey(name);
    }

    public static void clearVariables() {
        currentScope.variables.clear();
    }

    private static class Scope {
        private final Scope parent;

        public Scope(Scope parent) {
            this.parent = parent;
        }

        private static final HashMap<String, Value> variables = new HashMap<>();
    }
}
