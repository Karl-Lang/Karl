package fr.aiko.Karl.parser.ast.statements;

import java.util.ArrayList;

public class BlockStatement extends Statement {
    private final ArrayList<Statement> statements;

    public BlockStatement(ArrayList<Statement> statements) {
        this.statements = statements;
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    @Override
    public void eval() {
        for (Statement statement : statements) {
            statement.eval();
        }
    }
}
