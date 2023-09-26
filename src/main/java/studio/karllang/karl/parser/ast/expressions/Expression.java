package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.parser.ast.values.Value;

/** Represents an expression in Karl. */
public abstract class Expression {

  /**
   * Evaluates the expression.
   *
   * @return The value.
   */
  public abstract Value eval();
}
