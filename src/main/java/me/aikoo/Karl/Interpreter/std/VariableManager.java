package me.aikoo.Karl.Interpreter.std;

import me.aikoo.Karl.Interpreter.parser.ast.values.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public final class VariableManager {
    private static File currentFile;
    private final static ArrayList<File> files = new ArrayList<>();

    public static void addFile(String name) {
        currentFile = new File(name);
        files.add(currentFile);
    }

    public static File getCurrentFile() {
        return currentFile;
    }

    public static Optional<File> getFile(String name) {
        return files.stream().filter(file -> Objects.equals(file.name, name)).findFirst(); // orElse error
    }

    public static void clear() {
        files.clear();
    }

    public static class File {
        private Scope currentScope = new Scope(null);
        private final String name;

        public File(String name) {
            this.name = name;
        }

        public Value getVariable(String name) {
            HashMap<String, Value> variablesMap = new HashMap<>(currentScope.getVariables());
            variablesMap.putAll(currentScope.getFinalVariables());
            return variablesMap.get(name);
        }

        public boolean isFinal(String name) {
            return currentScope.getFinalVariables().containsKey(name);
        }

        public void setVariable(String name, Value value, boolean isFinal) {
            if (isFinal) {
                currentScope.getFinalVariables().put(name, value);
            } else {
                currentScope.getVariables().put(name, value);
            }
        }

        public void removeVariable(String name) {
            currentScope.getVariables().remove(name);
        }

        public boolean containsVariable(String name) {
            return currentScope.getVariables().containsKey(name);
        }

        public void clearVariables() {
            currentScope.getVariables().clear();
        }

        public void newScope() {
            Scope newScope = new Scope(currentScope);
            for (String key : currentScope.getVariables().keySet()) {
                newScope.getVariables().put(key, currentScope.getVariables().get(key));
            }
            currentScope = newScope;
        }

        public void exitScope() {
            if (currentScope.getParent() != null) {
                currentScope = currentScope.getParent();
            }
        }

        public Scope getScope() {
            return currentScope;
        }

        public void setScope(Scope scope) {
            currentScope = scope;
        }

        public void clear() {
            currentScope = new Scope(null);
        }
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