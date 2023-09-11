package studio.karllang.karl.lib.std.io;

import studio.karllang.karl.lib.Function;
import studio.karllang.karl.lib.Library;
import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.values.Value;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class io_Write extends Function {
  public io_Write(Library io) {
    super("Write", io);
  }

  @Override
  public void eval(ArrayList<Expression> expressions) {
    String result =
        expressions.stream()
            .map(Expression::eval)
            .map(Value::toString)
            .collect(Collectors.joining());

    System.out.print(result);
  }
}
