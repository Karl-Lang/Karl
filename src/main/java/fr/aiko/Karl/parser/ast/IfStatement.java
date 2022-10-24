package fr.aiko.Karl.parser.ast;

import java.util.ArrayList;

public class IfStatement extends Statement {
    private final boolean condition;
    private final ArrayList<Statement> statements;

    public IfStatement(boolean condition, ArrayList<Statement> statements) {
        this.condition = condition;
        this.statements = statements;
    }

    @Override
    public void execute() {
        if (condition) {
            for (Statement statement : statements) {
                statement.execute();
            }
        }
    }
}
