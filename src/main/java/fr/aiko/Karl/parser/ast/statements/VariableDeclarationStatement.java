package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.ast.expressions.VariableExpression;
import fr.aiko.Karl.std.VariableManager;

public class VariableDeclarationStatement extends Statement {
    private final VariableExpression expression;

    public VariableDeclarationStatement(VariableExpression expression) {
        this.expression = expression;
    }

    @Override
    public void eval() {
        System.out.println("VariableExpression.eval() : " + VariableManager.getScope());
        expression.eval();
    }
}
