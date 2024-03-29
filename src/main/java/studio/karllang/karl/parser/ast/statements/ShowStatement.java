package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.parser.ast.expressions.Expression;

import java.util.ArrayList;

public class ShowStatement extends Statement {
    private final ArrayList<Expression> expr;

    public ShowStatement(ArrayList<Expression> expr) {
        this.expr = expr;
    }

    @Override
    public void eval() {
        StringBuilder str = new StringBuilder();
        for (Expression e : expr) {
            String string = e.eval().toString();
            str.append(string);
        }
        System.out.println(str);
    }
}
