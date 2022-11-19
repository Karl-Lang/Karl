package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.std.VariableManager;

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
        VariableManager.Scope scope = VariableManager.getScope();
        VariableManager.newScope();
        for (Statement statement : statements) {
            statement.eval();
        }
        VariableManager.setScope(scope);
    }
}
