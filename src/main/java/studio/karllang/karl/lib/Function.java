package studio.karllang.karl.lib;

import java.util.ArrayList;
import studio.karllang.karl.parser.ast.expressions.Expression;

public abstract class Function {
  private final String name;
  private final Library library;

  public Function(String name, Library library) {
    this.name = name;
    this.library = library;
  }

  public abstract void eval(ArrayList<Expression> expressions);

  public String getName() {
    return name;
  }

  public Library getLibrary() {
    return library;
  }
}
