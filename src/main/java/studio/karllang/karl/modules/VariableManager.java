package studio.karllang.karl.modules;

import java.util.HashMap;
import java.util.LinkedHashMap;
import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a variable in Karl. */
public class VariableManager {
  private final File file;
  private final LinkedHashMap<String, Variable> exportedVariables = new LinkedHashMap<>();
  private Scope currentScope = new Scope(null);

  /**
   * Constructs a new VariableManager object with the specified file.
   *
   * @param file The file.
   */
  public VariableManager(File file) {
    this.file = file;
  }

  /**
   * Returns the variable with the specified name.
   *
   * @param name The name.
   * @return The variable.
   */
  public Variable getVariable(String name) {
    return currentScope.getVariables().get(name);
  }

  /**
   * Checks if the variable with the specified name is final.
   *
   * @param name The name.
   * @return The variable.
   */
  public boolean isFinal(String name) {
    return currentScope.getVariables().containsKey(name)
        && currentScope.getVariables().get(name).isFinal();
  }

  /**
   * Sets the variable with the specified name.
   *
   * @param name The name.
   * @param value The value.
   * @param isFinal If the variable is final.
   * @param isDeclaration If the variable is a declaration.
   * @param line The line number.
   * @param pos The position.
   */
  public void setVariable(
      String name, Value value, boolean isFinal, boolean isDeclaration, int line, int pos) {
    currentScope
        .getVariables()
        .put(name, new Variable(value.getType(), name, value, isFinal, isDeclaration));

    if (!isDeclaration) {
      if (currentScope.getParent() != null) {
        new RuntimeError(
            "Can't export a variable which is inside a block", file.getStringPath(), line, pos);
      }

      exportedVariables.put(name, new Variable(value.getType(), name, value, isFinal, false));
    }
  }

  /**
   * Checks if the variable with the specified name is declared.
   *
   * @param name The name.
   * @return The variable.
   */
  public boolean containsVariable(String name) {
    return currentScope.getVariables().containsKey(name);
  }

  /** Creates a new scope. */
  public void newScope() {
    Scope newScope = new Scope(currentScope);
    for (String key : currentScope.getVariables().keySet()) {
      newScope.getVariables().put(key, currentScope.getVariables().get(key));
    }
    currentScope = newScope;
  }

  /**
   * Get the current scope.
   *
   * @return The current scope.
   */
  public Scope getScope() {
    return currentScope;
  }

  /**
   * Set the current scope.
   *
   * @param scope The scope.
   */
  public void setScope(Scope scope) {
    currentScope = scope;
  }

  /** Clear the current scope. */
  public void clear() {
    currentScope = new Scope(null);
  }

  /**
   * Return the file.
   *
   * @return The file
   */
  public File getFile() {
    return this.file;
  }

  /** Represents a variable scope in Karl. */
  public static class Scope {
    private final Scope parent;
    private final HashMap<String, Variable> variables = new HashMap<>();
    private final HashMap<String, Variable> exportedVariables = new HashMap<>();

    /**
     * Constructs a new Scope object with the specified parent.
     *
     * @param parent The parent.
     */
    public Scope(Scope parent) {
      this.parent = parent;
    }

    /**
     * Returns the parent.
     *
     * @return The parent.
     */
    public Scope getParent() {
      return parent;
    }

    /**
     * Returns the variables.
     *
     * @return The variables.
     */
    public HashMap<String, Variable> getVariables() {
      return variables;
    }
  }
}
