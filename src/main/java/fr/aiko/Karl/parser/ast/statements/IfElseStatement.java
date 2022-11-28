package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.ast.expressions.Expression;
import fr.aiko.Karl.parser.ast.values.Value;

public class IfElseStatement extends Statement {
    private final Expression condition;
    private final BlockStatement ifStatement;
    private final Statement elseStatement;
    private Value returnValue = null;

    public IfElseStatement(Expression condition, BlockStatement ifStatement, Statement elseStatement) {
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
            if (ifStatement.getResult() != null) {
                returnValue = ifStatement.getResult();
            }
        } else {
            if (elseStatement != null) {
                elseStatement.eval();
                if (elseStatement instanceof BlockStatement) {
                    if (((BlockStatement) elseStatement).getResult() != null) {
                        returnValue = ((BlockStatement) elseStatement).getResult();
                    }
                }
            }
        }
    }

    public Value getResult() {
        return returnValue;
    }
}
