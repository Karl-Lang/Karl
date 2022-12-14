package fr.aiko.Karl.std;

import fr.aiko.Karl.parser.ast.values.Value;

import java.util.HashMap;
import java.util.Map;

public final class VariableManager {

    private static Scope currentScope = new Scope(null);

    public static Value getVariable(String name) {
        HashMap<String, Value> variablesMap = new HashMap<>(currentScope.getVariables());
        variablesMap.putAll(currentScope.getFinalVariables());
        return variablesMap.get(name);
    }

    public static boolean isFinal(String name) {
        return currentScope.getFinalVariables().containsKey(name);
    }

    public static void setVariable(String name, Value value, boolean isFinal) {
        if (isFinal) {
            currentScope.getFinalVariables().put(name, value);
        } else {
            currentScope.getVariables().put(name, value);
        }
    }

    public static void removeVariable(String name) {
        currentScope.getVariables().remove(name);
    }

    public static boolean containsVariable(String name) {
        return currentScope.getVariables().containsKey(name);
    }

    public static void clearVariables() {
        currentScope.getVariables().clear();
    }

    public static void newScope() {
        Scope newScope = new Scope(currentScope);
        for (String key : currentScope.getVariables().keySet()) {
            newScope.getVariables().put(key, currentScope.getVariables().get(key));
        }
        currentScope = newScope;
    }

    public static void exitScope() {
        if (currentScope.getParent() != null) {
            currentScope = currentScope.getParent();
        }
    }

    public static Scope getScope() {
        return currentScope;
    }

    public static void setScope(Scope scope) {
        currentScope = scope;
    }

    public static void clear() {
        currentScope = new Scope(null);
    }

    public static class Scope {
        private final Scope parent;
        private final HashMap<String, Value> variables = new HashMap<>();
        private final HashMap<String, Value> finalVariables = new HashMap<>();

        public Scope(Scope parent) {
            this.parent = parent;
        }

        public Scope getParent() {
            return parent;
        }

        public HashMap<String, Value> getVariables() {
            // return variables and finalVariables
            return variables;
        }

        public HashMap<String, Value> getFinalVariables() {
            return finalVariables;
        }
    }
}