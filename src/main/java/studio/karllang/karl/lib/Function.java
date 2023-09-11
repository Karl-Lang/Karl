package studio.karllang.karl.lib;

import studio.karllang.karl.parser.ast.expressions.Expression;

import java.util.ArrayList;

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
