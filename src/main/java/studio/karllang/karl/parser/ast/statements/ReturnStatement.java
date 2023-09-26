package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a return statement in Karl. */
public class ReturnStatement extends Statement {
  private final Expression expr;
  private Value result;

  /**
   * Constructs a new ReturnStatement object with the specified expression.
   *
   * @param expr The expression.
   * @param line The line number.
   * @param pos The position.
   */
  public ReturnStatement(Expression expr, int line, int pos) {
    super(line, pos);
    this.expr = expr;
  }

  /** Evaluates the return statement. */
  @Override
  public void eval() {
    result = expr.eval();
  }

  /**
   * Returns the result.
   *
   * @return The result.
   */
  public Value getResult() {
    return result;
  }
}
