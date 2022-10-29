package fr.aiko.Karl.parser.ast;

import fr.aiko.Karl.parser.Parser;

import java.util.ArrayList;

public class IfStatement extends Statement {
    private final boolean condition;
    private final ArrayList<Statement> statements;
    private final ArrayList<Statement> elseStatements;

    public IfStatement(boolean condition, ArrayList<Statement> statements, ArrayList<Statement> elseStatements) {
        this.condition = condition;
        this.statements = statements;
        this.elseStatements = elseStatements;
    }

    @Override
    public void execute() {
        if (condition) {
            for (Statement statement : statements) {
                statement.execute();
            }
        } else if (elseStatements != null) {
            for (Statement statement : elseStatements) {
                statement.execute();
            }
        }
    }
}
