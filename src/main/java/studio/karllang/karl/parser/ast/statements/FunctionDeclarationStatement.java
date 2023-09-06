package studio.karllang.karl.parser.ast.statements;

import java.util.HashMap;
import studio.karllang.karl.parser.TokenType;

public class FunctionDeclarationStatement extends Statement {
  private final String name;
  private final HashMap<String, TokenType> args;
  private final BlockStatement body;
  private final TokenType type;

  public FunctionDeclarationStatement(
      String name, HashMap<String, TokenType> args, TokenType returnType, BlockStatement block) {
    this.name = name;
    this.args = args;
    this.body = block;
    this.type = returnType;
  }

  @Override
  public void eval() {}
}
