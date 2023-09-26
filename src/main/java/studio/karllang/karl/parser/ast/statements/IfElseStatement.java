package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents an if-else statement in Karl. */
public class IfElseStatement extends Statement {
  private final Expression condition;
  private final BlockStatement ifStatement;
  private final Statement elseStatement;
  private Value returnValue = null;

  /**
   * Constructs a new IfElseStatement object with the specified condition, if statement, and else
   * statement.
   *
   * @param condition The condition.
   * @param ifStatement The if statement.
   * @param elseStatement The else statement.
   * @param line The line number.
   * @param pos The position.
   */
  public IfElseStatement(
      Expression condition,
      BlockStatement ifStatement,
      Statement elseStatement,
      int line,
      int pos) {
    super(line, pos);
    this.condition = condition;
    this.ifStatement = ifStatement;
    this.elseStatement = elseStatement;
  }

  /** Evaluates the if-else statement. */
  @Override
  public void eval() {
    if (Boolean.parseBoolean(condition.eval().toString())) {
      ifStatement.eval();
      if (ifStatement.getResult() != null) {
        returnValue = ifStatement.getResult();
      }
    } else {
      if (elseStatement != null) {
        elseStatement.eval();
        if (elseStatement instanceof BlockStatement) {
          if (((BlockStatement) elseStatement).getResult() != null) {
            returnValue = ((BlockStatement) elseStatement).getResult();
          }
        } else if (elseStatement instanceof IfElseStatement) {
          if (((IfElseStatement) elseStatement).getResult() != null) {
            returnValue = ((IfElseStatement) elseStatement).getResult();
          }
        }
      }
    }
  }

  /**
   * Returns the result of the if-else statement.
   *
   * @return The result.
   */
  public Value getResult() {
    return returnValue;
  }
}
