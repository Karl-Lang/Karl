package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.lib.Library;
import studio.karllang.karl.lib.LibraryManager;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a library call expression in Karl. */
public class LibCallExpression extends Expression {
  private final String name;
  private final File file;
  private final int line;
  private final int pos;
  private LibCallExpression child;

  /**
   * Constructs a new LibCallExpression object with the specified name, file, line and position.
   *
   * @param name The name.
   * @param file The file.
   * @param line The line number.
   * @param pos The position.
   */
  public LibCallExpression(String name, File file, int line, int pos) {
    this.name = name;
    this.file = file;
    this.line = line;
    this.pos = pos;
  }

  /**
   * Adds a child to the LibCallExpression.
   *
   * @param child The child.
   */
  public void addChild(LibCallExpression child) {
    this.child = child;
  }

  /**
   * Returns the child.
   *
   * @return The child.
   */
  public LibCallExpression getChild() {
    return child;
  }

  /**
   * Returns the name.
   *
   * @return The name.
   */
  public String getName() {
    return name;
  }

  /**
   * Evaluates the library call expression.
   *
   * @return The result.
   */
  @Override
  public Value eval() {
    if (child == null) {
      LibraryManager.importLibrary(name, file, line, pos);
    } else {
      Library library = LibraryManager.getLibrary(name);
      Library importedLib = library;
      LibCallExpression childClass = child;
      while (childClass != null) {
        LibCallExpression finalChildClass = childClass;
        Library subLib =
            library.getSubLibraries().stream()
                .filter(n -> n.getName().equals(finalChildClass.getName()))
                .findAny()
                .orElse(null);
        if (subLib == null) {
          new RuntimeError(
              "Unknown library: " + childClass.getName(), file.getStringPath(), line, pos);
          return null;
        }
        importedLib = subLib;
        childClass = childClass.getChild();
      }

      LibraryManager.addImportedLibrary(importedLib, file, line, pos);
    }

    return null;
  }
}
