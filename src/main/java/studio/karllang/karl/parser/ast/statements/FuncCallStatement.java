package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.parser.ast.expressions.FuncCallExpression;

/** Represents a function call statement in Karl. */
public class FuncCallStatement extends Statement {
  private final FuncCallExpression expression;

  /**
   * Constructs a new FuncCallStatement object with the specified function call expression.
   *
   * @param expression The function call expression.
   * @param line The line number.
   * @param pos The position.
   */
  public FuncCallStatement(FuncCallExpression expression, int line, int pos) {
    super(line, pos);
    this.expression = expression;
  }

  /** Evaluates the function call statement. */
  @Override
  public void eval() {
    expression.eval();
  }
}
