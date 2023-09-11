package studio.karllang.karl.parser.ast.statements;

public abstract class Statement {

  private final int line;
  private final int pos;

  public Statement(int line, int pos) {
    this.line = line;
    this.pos = pos;
  }

  public abstract void eval();

  public int getLine() {
    return line;
  }

  public int getPos() {
    return pos;
  }
}
