package me.aikoo.Karl.parser.ast.statements;

import me.aikoo.Karl.parser.ast.expressions.FuncCallExpression;

public class FuncCallStatement extends Statement {
    private final FuncCallExpression expression;

    public FuncCallStatement(FuncCallExpression expression) {
        this.expression = expression;
    }

    @Override
    public void eval() {
        expression.eval();
    }
}
