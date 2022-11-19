package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.expressions.Expression;
import fr.aiko.Karl.parser.ast.expressions.FuncCallExpression;
import fr.aiko.Karl.std.Function;
import fr.aiko.Karl.std.FunctionManager;

import java.util.ArrayList;
import java.util.HashMap;

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
