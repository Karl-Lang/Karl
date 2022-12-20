package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.parser.ast.expressions.FuncCallExpression;

public class FuncCallStatement extends Statement {
    private final FuncCallExpression expression;

    public FuncCallStatement(FuncCallExpression expression) {
        this.expression = expression;
    }

    @Override
    public void eval() throws RuntimeError {
        expression.eval();
    }
}
