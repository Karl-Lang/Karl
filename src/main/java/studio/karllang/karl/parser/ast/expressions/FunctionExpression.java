package studio.karllang.karl.parser.ast.expressions;

import java.util.LinkedHashMap;
import studio.karllang.karl.modules.File;
import studio.karllang.karl.parser.TokenType;
import studio.karllang.karl.parser.ast.statements.BlockStatement;
import studio.karllang.karl.parser.ast.values.Value;

/** Represents a function expression in Karl. */
public class FunctionExpression extends Expression {

  private final String name;
  private final LinkedHashMap<String, TokenType> args;
  private final BlockStatement body;
  private final TokenType type;
  private final File file;

  /**
   * Constructs a new FunctionExpression object with the specified name, args, returnType, block and
   * file.
   *
   * @param name The name.
   * @param args The args.
   * @param returnType The returnType.
   * @param block The block.
   * @param file The file.
   */
  public FunctionExpression(
      String name,
      LinkedHashMap<String, TokenType> args,
      TokenType returnType,
      BlockStatement block,
      File file) {
    this.name = name;
    this.args = args;
    this.body = block;
    this.file = file;
    this.type = returnType;
  }

  @Override
  public Value eval() {
    return null;
  }
}
