package me.aikoo.Karl.parser.ast.statements;

import me.aikoo.Karl.parser.ast.expressions.Expression;
import me.aikoo.Karl.parser.ast.values.Value;

public class ReturnStatement extends Statement {
    private final Expression expr;
    private Value result;

    public ReturnStatement(Expression expr) {
        this.expr = expr;
    }

    @Override
    public void eval() {
        result = expr.eval();
    }

    public Value getResult() {
        return result;
    }
}
