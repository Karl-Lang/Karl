package studio.karllang.karl.modules;

import java.util.LinkedHashMap;

/** Represents a function manager in Karl. */
public class FunctionManager {
  private final File file;
  private final LinkedHashMap<String, Function> functions = new LinkedHashMap<>();
  private final LinkedHashMap<String, Function> exportedFunctions = new LinkedHashMap<>();

  /**
   * Constructs a new FunctionManager object with the specified file.
   *
   * @param file The file.
   */
  public FunctionManager(File file) {
    this.file = file;
  }

  /**
   * Add a function to the function manager.
   *
   * @param function The function.
   * @param isDeclared If the function is declared.
   */
  public void addFunction(Function function, boolean isDeclared) {
    functions.put(function.getName(), function);
    if (!isDeclared) {
      exportedFunctions.put(function.getName(), function);
    }
  }

  /**
   * Returns the function with the specified name.
   *
   * @param name The name.
   * @return The function.
   */
  public Function getFunction(String name) {
    return functions.get(name);
  }

  /**
   * Returns the exported function with the specified name.
   *
   * @param name The name.
   * @return The function.
   */
  public boolean isFunction(String name) {
    return functions.containsKey(name);
  }

  /** Returns the exported function with the specified name. */
  public void clear() {
    functions.clear();
  }

  /**
   * Returns the file.
   *
   * @return The file.
   */
  public File getFile() {
    return file;
  }
}
