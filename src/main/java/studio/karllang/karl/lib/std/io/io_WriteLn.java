package studio.karllang.karl.lib.std.io;

import java.util.ArrayList;
import java.util.stream.Collectors;
import studio.karllang.karl.lib.Function;
import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.values.Value;

public class io_WriteLn extends Function {

  public io_WriteLn(Io io) {
    super("WriteLn", io);
  }

  @Override
  public void eval(ArrayList<Expression> expressions) {
    String result =
        expressions.stream()
            .map(Expression::eval)
            .map(Value::toString)
            .collect(Collectors.joining());

    System.out.println(result);
  }
}
