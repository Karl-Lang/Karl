package studio.karllang.karl.std;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.parser.ast.values.Value;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class VariableManager {

    private final File file;
    private Scope currentScope = new Scope(null);
    private final LinkedHashMap<String, Variable> exportedVariables = new LinkedHashMap<>();

    public VariableManager(File file) {
        this.file = file;
    }

    public Variable getVariable(String name) {
        return currentScope.getVariables().get(name);
    }

    public boolean isFinal(String name) {
        return currentScope.getVariables().containsKey(name) && currentScope.getVariables().get(name).isFinal();
    }

    public void setVariable(String name, Value value, boolean isFinal, boolean isDeclaration, int line, int pos) {
        currentScope.getVariables().put(name, new Variable(value.getType(), name, value, isFinal, isDeclaration));

        if (!isDeclaration) {
            if (currentScope.getParent() != null) {
                new RuntimeError("Can't export a variable which is inside a block", file.getStringPath(), line, pos);
            }

            exportedVariables.put(name, new Variable(value.getType(), name, value, isFinal, false));
        }
    }

    public LinkedHashMap<String, Variable> getExportedVariables() {
        return exportedVariables;
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

    public File getFile() {
        return this.file;
    }

    public static class Scope {
        private final Scope parent;
        private final HashMap<String, Variable> variables = new HashMap<>();
        private final HashMap<String, Variable> exportedVariables = new HashMap<>();

        public Scope(Scope parent) {
            this.parent = parent;
        }

        public Scope getParent() {
            return parent;
        }

        public HashMap<String, Variable> getVariables() {
            return variables;
        }
    }
}
