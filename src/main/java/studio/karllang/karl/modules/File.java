package studio.karllang.karl.modules;

import java.nio.file.Path;

/**
 * Represents a file in the Karl programming environment. A 'File' object encapsulates information
 * about a script or source code file, including its name, extension, path, and management of
 * functions and variables.
 */
public class File {

  private final FunctionManager functionManager; // Manages functions defined within the file.
  private final String name; // The name of the file.
  private final String extension; // The file extension.
  private final Path path; // The file path.
  private final String pathStr; // The file path as a string.
  private final VariableManager variableManager; // Manages variables within the file.

  /**
   * Constructs a new 'File' object with the specified name, extension, and path.
   *
   * @param name The name of the file.
   * @param extension The file extension.
   * @param pathStr The path of the file as a string.
   */
  public File(String name, String extension, String pathStr) {
    this.name = name;
    this.extension = extension;
    this.functionManager = new FunctionManager(this);
    this.pathStr = pathStr;
    this.path = Path.of(pathStr);
    this.variableManager = new VariableManager(this);
  }

  /**
   * Returns the name of the file.
   *
   * @return The name of the file.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the file extension.
   *
   * @return The file extension.
   */
  public String getExtension() {
    return extension;
  }

  /**
   * Returns the path to the file.
   *
   * @return The file path.
   */
  public Path getPath() {
    return path;
  }

  /**
   * Returns the file path as a string.
   *
   * @return The file path as a string.
   */
  public String getStringPath() {
    return pathStr;
  }

  /**
   * Returns the manager responsible for functions defined within the file.
   *
   * @return The function manager.
   */
  public FunctionManager getFunctionManager() {
    return functionManager;
  }

  /**
   * Returns the manager responsible for variables within the file.
   *
   * @return The variable manager.
   */
  public VariableManager getVariableManager() {
    return variableManager;
  }
}
