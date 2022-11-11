package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.ast.expressions.Expression;

public class VariableAssignmentStatement extends Statement {
    private final Expression expression;

    public VariableAssignmentStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void eval() {
        expression.eval();
    }
}
