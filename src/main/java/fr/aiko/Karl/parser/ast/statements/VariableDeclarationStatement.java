package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.ast.expressions.VariableExpression;

public class VariableDeclarationStatement extends Statement {
    private final VariableExpression expression;

    public VariableDeclarationStatement(VariableExpression expression) {
        this.expression = expression;
    }

    @Override
    public void eval() {
        expression.eval();
    }
}
