package studio.karllang.karl.lib.std.io;

import studio.karllang.karl.lib.Function;
import studio.karllang.karl.parser.ast.expressions.Expression;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class io_WriteLn extends Function {

    public io_WriteLn(Io io) {
        super("WriteLn", io);
    }
    @Override
    public void eval(ArrayList<Expression> expressions) {
        String result = expressions.stream()
                .map(Expression::eval)
                .map(Object::toString)
                .collect(Collectors.joining());

        System.out.println(result);

    }
}
