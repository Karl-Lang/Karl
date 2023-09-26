package studio.karllang.karl.parser.ast.statements;

import java.util.ArrayList;
import java.util.HashMap;
import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.modules.VariableManager;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a block statement in Karl. */
public class BlockStatement extends Statement {
  private final ArrayList<Statement> statements;
  private final File file;
  private final int line;
  private final int pos;
  private Value result;
  private HashMap<String, Value> args;

  /**
   * Constructs a new BlockStatement object with the specified statements.
   *
   * @param statements The statements.
   * @param file The file.
   * @param line The line.
   * @param pos The position.
   */
  public BlockStatement(ArrayList<Statement> statements, File file, int line, int pos) {
    super(line, pos);
    this.statements = statements;
    this.file = file;
    this.line = line;
    this.pos = pos;
  }

  /**
   * Sets the arguments for this block statement.
   *
   * @param args The arguments.
   */
  public void setArgs(HashMap<String, Value> args) {
    this.args = args;
  }

  /**
   * Returns the statements.
   *
   * @return The statements.
   */
  public ArrayList<Statement> getStatements() {
    return statements;
  }

  /** Evaluates the block statement. */
  @Override
  public void eval() {
    VariableManager.Scope scope = this.file.getVariableManager().getScope();
    this.file.getVariableManager().newScope();
    if (args != null) {
      for (String arg : args.keySet()) {
        this.file.getVariableManager().setVariable(arg, args.get(arg), false, true, line, pos);
      }
    }
    for (Statement statement : statements) {
      statement.eval();

      if (statement instanceof IfElseStatement) {
        if (((IfElseStatement) statement).getResult() != null) {
          result = ((IfElseStatement) statement).getResult();
          break;
        }
      } else if (statement instanceof ReturnStatement) {
        result = ((ReturnStatement) statement).getResult();

        if (statements.indexOf(statement) != statements.size() - 1) {
          new RuntimeError(
              "Unreachable code after return statement", file.getStringPath(), line, pos);
        }
        break;
      }
    }

    for (String var : scope.getVariables().keySet()) {
      if (!this.file.getVariableManager().getVariable(var).equals(scope.getVariables().get(var))) {
        scope.getVariables().put(var, this.file.getVariableManager().getVariable(var));
      }
    }
    this.file.getVariableManager().setScope(scope);
  }

  /**
   * Returns the result of the block statement.
   *
   * @return The result.
   */
  public Value getResult() {
    return result;
  }
}
