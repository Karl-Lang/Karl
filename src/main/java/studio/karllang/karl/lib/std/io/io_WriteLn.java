package studio.karllang.karl.lib.std.io;

import studio.karllang.karl.lib.Function;
import studio.karllang.karl.parser.ast.expressions.Expression;

import java.util.ArrayList;

public class io_WriteLn extends Function {

    public io_WriteLn(Io io) {
        super("WriteLn", io);
    }
    @Override
    public void eval(ArrayList<Expression> expressions) {
        StringBuilder str = new StringBuilder();
        for (Expression e : expressions) {
            String string = e.eval().toString();
            str.append(string);
        }
        System.out.println(str);
    }
}
