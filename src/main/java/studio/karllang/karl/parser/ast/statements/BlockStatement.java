package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.VariableManager;

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
        VariableManager.Scope scope = VariableManager.getCurrentFile().getScope();
        VariableManager.getCurrentFile().newScope();
        if (args != null) {
            for (String arg : args.keySet()) {
                VariableManager.getCurrentFile().setVariable(arg, args.get(arg), false);
            }
        }
        for (Statement statement : statements) {
            statement.eval();

            if (statement instanceof IfElseStatement) {
                if (((IfElseStatement) statement).getResult() != null) {
                    result = ((IfElseStatement) statement).getResult();
                    break;
                }
            } else if (statement instanceof ReturnStatement) {
                result = ((ReturnStatement) statement).getResult();
                break;
            }
        }

        for (String var : scope.getVariables().keySet()) {
            if (!VariableManager.getCurrentFile().getVariable(var).equals(scope.getVariables().get(var))) {
                scope.getVariables().put(var, VariableManager.getCurrentFile().getVariable(var));
            }
        }
        VariableManager.getCurrentFile().setScope(scope);
    }

    public Value getResult() {
        return result;
    }
}
