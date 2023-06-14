package studio.karllang.karl.parser.ast.statements;

import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.File;
import studio.karllang.karl.std.VariableManager;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockStatement extends Statement {
    private final ArrayList<Statement> statements;
    private Value result;
    private HashMap<String, Value> args;
    private final File file;

    public BlockStatement(ArrayList<Statement> statements, File file) {
        this.statements = statements;
        this.file = file;
    }

    public void setArgs(HashMap<String, Value> args) {
        this.args = args;
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    @Override
    public void eval() {
        VariableManager.Scope scope = this.file.getVariableManager().getScope();
        this.file.getVariableManager().newScope();
        if (args != null) {
            for (String arg : args.keySet()) {
                this.file.getVariableManager().setVariable(arg, args.get(arg), false);
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
            if (!this.file.getVariableManager().getVariable(var).equals(scope.getVariables().get(var))) {
                scope.getVariables().put(var, this.file.getVariableManager().getVariable(var));
            }
        }
        this.file.getVariableManager().setScope(scope);
    }

    public Value getResult() {
        return result;
    }
}
