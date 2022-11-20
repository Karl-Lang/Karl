package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.ast.expressions.FuncCallExpression;

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
