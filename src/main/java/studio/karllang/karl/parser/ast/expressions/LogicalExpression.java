package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.RuntimeError.RuntimeError;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.modules.LogicalOperators;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.values.BooleanValue;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a logical expression in Karl. */
public class LogicalExpression extends Expression {
  private final Expression left;
  private final Expression right;
  private final TokenType operator;
  private final File file;
  private final int line;
  private final int pos;

  /**
   * Constructs a new LogicalExpression object with the specified left, right, operator, file, line
   * and position.
   *
   * @param operator The operator.
   * @param left The left expression.
   * @param right The right expression.
   * @param file The file.
   * @param line The line number.
   * @param pos The position.
   */
  public LogicalExpression(
      TokenType operator, Expression left, Expression right, File file, int line, int pos) {
    this.left = left;
    this.right = right;
    this.operator = operator;
    this.file = file;
    this.line = line;
    this.pos = pos;
  }

  /**
   * Evaluates the logical expression.
   *
   * @return The result.
   */
  @Override
  public Value eval() {
    Value leftValue = left.eval();
    if (operator != null) {
      if (right != null) {
        Value rightValue = right.eval();
        if (leftValue.getType() == TokenType.NULL || rightValue.getType() == TokenType.NULL) {
          return switch (operator) {
            case NOT_EQUAL -> {
              if (leftValue.getType() == TokenType.NULL && rightValue.getType() == TokenType.NULL)
                yield new BooleanValue(false);
              else yield new BooleanValue(true);
            }
            case EQUALEQUAL -> {
              if (leftValue.getType() == TokenType.NULL && rightValue.getType() == TokenType.NULL)
                yield new BooleanValue(true);
              else yield new BooleanValue(false);
            }
            default -> {
              new RuntimeError(
                  "Bad operator: " + operator.getName(), file.getStringPath(), line, pos);
              yield null;
            }
          };

        } else if ((leftValue.getType() != TokenType.INT_VALUE
                && leftValue.getType() != TokenType.FLOAT_VALUE)
            || (rightValue.getType() != TokenType.INT_VALUE
                && rightValue.getType() != TokenType.FLOAT_VALUE)) {
          final boolean equals = leftValue.toString().equals(rightValue.toString());

          return switch (operator) {
            case AND -> new BooleanValue(
                LogicalOperators.and(
                    Boolean.parseBoolean(leftValue.toString()),
                    Boolean.parseBoolean(rightValue.toString())));
            case OR -> new BooleanValue(
                LogicalOperators.or(
                    Boolean.parseBoolean(leftValue.toString()),
                    Boolean.parseBoolean(rightValue.toString())));
            case EQUALEQUAL -> new BooleanValue(equals);
            case NOT_EQUAL -> new BooleanValue(!equals);
            default -> {
              new RuntimeError("Unknown operator: " + operator, file.getStringPath(), line, pos);
              yield null;
            }
          };

        } else {
          return new BooleanValue(
              LogicalOperators.compare(leftValue, rightValue, operator, file, line, pos));
        }
      } else {
        if (operator == TokenType.EXCLAMATION) {
          return new BooleanValue(!Boolean.parseBoolean(leftValue.toString()));
        } else {
          new RuntimeError("Unknown operator: " + operator, file.getStringPath(), line, pos);
          return null;
        }
      }
    } else {
      return new BooleanValue(Boolean.parseBoolean(leftValue.toString()));
    }
  }
}
