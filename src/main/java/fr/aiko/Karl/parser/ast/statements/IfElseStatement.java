package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.ast.expressions.Expression;

public class IfElseStatement extends Statement {
    private final Expression condition;
    private final Statement ifStatement;
    private final Statement elseStatement;

    public IfElseStatement(Expression condition, Statement ifStatement, Statement elseStatement) {
        this.condition = condition;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    public Statement getIfStatement() {
        return ifStatement;
    }

    public Statement getElseStatement() {
        return elseStatement;
    }

    @Override
    public void eval() {
        if (Boolean.parseBoolean(condition.eval().toString())) {
            ifStatement.eval();
        } else {
            if (elseStatement != null) elseStatement.eval();
        }
    }
}
