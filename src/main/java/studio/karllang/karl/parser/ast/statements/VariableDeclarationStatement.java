package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.lib.LibraryManager;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.modules.ForbiddenNames;
import studio.karllang.karl.modules.Types;
import studio.karllang.karl.parser.Token;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.expressions.ValueExpression;
import studio.karllang.karl.parser.ast.expressions.VariableExpression;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a variable declaration statement in Karl. */
public class VariableDeclarationStatement extends Statement {
  private final String name;
  private final Token type;
  private final File file;
  private final String fileName;
  private final int line;
  private final int pos;
  private final boolean isFinal;
  private final boolean isDeclaration;
  private Expression expression;

  /**
   * Constructs a new VariableDeclarationStatement object with the specified expression, name, type,
   * file, line, position, isFinal and isDeclaration.
   *
   * @param expression The expression.
   * @param name The name.
   * @param type The type.
   * @param file The file.
   * @param line The line number.
   * @param pos The position.
   * @param isFinal The isFinal.
   * @param isDeclaration The isDeclaration.
   */
  public VariableDeclarationStatement(
      Expression expression,
      String name,
      Token type,
      File file,
      int line,
      int pos,
      boolean isFinal,
      boolean isDeclaration) {
    super(line, pos);
    this.expression = expression;
    this.name = name;
    this.type = type;
    this.file = file;
    this.fileName = file.getName();
    this.line = line;
    this.pos = pos;
    this.isFinal = isFinal;
    this.isDeclaration = isDeclaration;
  }

  /** Evaluates the variable declaration statement. */
  @Override
  public void eval() {
    if (ForbiddenNames.isForbiddenName(name)) {
      new RuntimeError("Variable name " + name + " is forbidden", file.getStringPath(), line, pos);
    }

    if (this.file.getVariableManager().getVariable(name) != null) {
      new RuntimeError(
          "Variable " + name + " is already declared", file.getStringPath(), line, pos);
    }

    Value value = expression.eval();
    if (type.getType() == TokenType.FLOAT && value.getType() == TokenType.INT_VALUE) {
      expression = new ValueExpression(value.toFloat(), TokenType.FLOAT_VALUE);
      value = expression.eval();
    }

    if (value.toString().equals("null_void")) {
      new RuntimeError(
          "Cannot assign void function to a variable", file.getStringPath(), line, pos);
    }

    if (!Types.checkValueType(type.getType(), value.getType())
        && value.getType() != TokenType.NULL) {
      new RuntimeError(
          "Expected type "
              + Types.getTypeName(type.getType())
              + " but got "
              + Types.getTypeName(value.getType()),
          file.getStringPath(),
          line,
          pos - 1);
    }

    if (value.getType() == TokenType.NULL
        && type.getType() != TokenType.STRING
        && type.getType() != TokenType.CHAR) {
      new RuntimeError(
          Types.getTypeName(type.getType()) + " variable cannot be null",
          file.getStringPath(),
          line,
          pos - 1);
    }

    checkName(name, file, line, pos);

    VariableExpression expr =
        new VariableExpression(name, value, isFinal, file, isDeclaration, line, pos);
    // expr.setValue(value);
    expr.eval();
  }

  /**
   * Checks if the name is valid.
   *
   * @param name The name.
   * @param file The file.
   * @param line The line number.
   * @param pos The position.
   */
  private void checkName(String name, File file, int line, int pos) {
    if (ForbiddenNames.isForbiddenName(name)) {
      new RuntimeError("Variable name " + name + " is forbidden", file.getStringPath(), line, pos);
    } else if (file.getFunctionManager().isFunction(name)) {
      new RuntimeError(
          "Variable name " + name + " is already declared as a function",
          file.getStringPath(),
          line,
          pos);
    } else if (file.getVariableManager().containsVariable(name)) {
      new RuntimeError(
          "Variable name " + name + " is already declared", file.getStringPath(), line, pos);
    } else if (LibraryManager.getImportedLibrairies().stream()
        .anyMatch(n -> n.getName().equals(name))) {
      new RuntimeError(
          "Cannot set a variable name that is the same than a library: " + name,
          file.getStringPath(),
          line,
          pos);
    }
  }
}
