package studio.karllang.karl.parser.ast.statements;

/** Represents a statement in Karl. */
public abstract class Statement {
  private final int line;
  private final int pos;

  /**
   * Constructs a new Statement object with the specified line number and position.
   *
   * @param line The line number.
   * @param pos The position.
   */
  public Statement(int line, int pos) {
    this.line = line;
    this.pos = pos;
  }

  /** Evaluates the statement. */
  public abstract void eval();

  /**
   * Returns the line number.
   *
   * @return The line number.
   */
  public int getLine() {
    return line;
  }

  /**
   * Returns the position.
   *
   * @return The position.
   */
  public int getPos() {
    return pos;
  }
}
