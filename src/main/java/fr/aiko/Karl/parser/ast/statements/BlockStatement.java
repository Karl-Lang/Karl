package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.VariableManager;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockStatement extends Statement {
    private final ArrayList<Statement> statements;
    private Value result;
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

            if (statement instanceof ReturnStatement) {
                result = ((ReturnStatement) statement).getResult();
                break;
            } else if (statement instanceof IfElseStatement) {
                result = ((IfElseStatement) statement).getResult();
                break;
            }
        }

        for (String var : scope.getVariables().keySet()) {
            if (!VariableManager.getVariable(var).equals(scope.getVariables().get(var))) {
                scope.getVariables().put(var, VariableManager.getVariable(var));
            }
        }
        VariableManager.setScope(scope);
    }

    public Value getResult() {
        return result;
    }
}
