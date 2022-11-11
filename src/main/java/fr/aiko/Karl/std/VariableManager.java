package fr.aiko.Karl.std;

import fr.aiko.Karl.parser.ast.values.Value;

import java.util.HashMap;

public final class VariableManager {
    // Create a new class scope to manage variables
    private static class Scope {
        private final Scope parent;
        private final HashMap<String, Value> variables;

        public Scope(Scope parent) {
            this.parent = parent;
            this.variables = new HashMap<>();
        }

        public Value getVariable(String name) {
            if (variables.containsKey(name)) {
                return variables.get(name);
            } else if (parent != null) {
                return parent.getVariable(name);
            } else {
                return null;
            }
        }

        public void setVariable(String name, Value value) {
            variables.put(name, value);
        }
    }

    private static Scope currentScope = new Scope(null);

    public static Value getVariable(String name) {
        return currentScope.getVariable(name);
    }

    public static void setVariable(String name, Value value) {
        currentScope.setVariable(name, value);
    }

    public static void enterScope() {
        currentScope = new Scope(currentScope);
    }

    public static void exitScope() {
        currentScope = currentScope.parent;
    }

    public static void clear() {
        currentScope = new Scope(null);
    }

    public static void reset() {
        clear();
    }
}
