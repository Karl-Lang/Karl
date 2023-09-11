package studio.karllang.karl.modules;

import java.nio.file.Path;

public class File {

  private final FunctionManager functionManager;
  private final String name;
  private final String extension;
  private final Path path;
  private final String pathStr;
  private final VariableManager variableManager;

  public File(String name, String extension, String pathStr) {
    this.name = name;
    this.extension = extension;
    this.functionManager = new FunctionManager(this);
    this.pathStr = pathStr;
    this.path = Path.of(pathStr);
    this.variableManager = new VariableManager(this);
  }

  public String getName() {
    return name;
  }

  public String getExtension() {
    return extension;
  }

  public Path getPath() {
    return path;
  }

  public String getStringPath() {
    return pathStr;
  }

  public FunctionManager getFunctionManager() {
    return functionManager;
  }

  public VariableManager getVariableManager() {
    return variableManager;
  }
}
