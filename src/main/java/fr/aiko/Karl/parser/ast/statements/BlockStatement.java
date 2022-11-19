package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.VariableManager;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockStatement extends Statement {
    private final ArrayList<Statement> statements;
    private HashMap<String, Value> args;

    public BlockStatement(ArrayList<Statement> statements) {
        this.statements = statements;
    }

    public void setArgs(HashMap<String, Value> args) {
        this.args = args;
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    @Override
    public void eval() {
        VariableManager.Scope scope = VariableManager.getScope();
        VariableManager.newScope();
        if (args != null) {
            for (String arg : args.keySet()) {
                VariableManager.setVariable(arg, args.get(arg));
            }
        }
        for (Statement statement : statements) {
            statement.eval();
        }
        VariableManager.setScope(scope);
    }
}
