package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.expressions.Expression;
import fr.aiko.Karl.std.Function;
import fr.aiko.Karl.std.FunctionManager;

import java.util.ArrayList;
import java.util.HashMap;

public class FuncCallStatement extends Statement {
    private final String name;
    private final ArrayList<Expression> args;
    private final String fileName;
    private final int line;
    private final int pos;

    public FuncCallStatement(String name, ArrayList<Expression> args, String fileName, int line, int pos) {
        this.name = name;
        this.args = args;
        this.fileName = fileName;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public void eval() {
        if (!FunctionManager.isFunction(name)) {
            new RuntimeError("Unknown function: " + name, fileName, line, pos);
        }

        Function function = FunctionManager.getFunction(name);
        HashMap<String, TokenType> parameters = function.getArgs();
        if (args.size() != parameters.size()) {
            new RuntimeError("Function " + name + " takes " + parameters.size() + " arguments, " + args.size() + " given", fileName, line, pos);
        }

        int i = 0;
        for (String arg : function.getArgs().keySet()) {
            if (function.getArgs().get(arg) != args.get(i).eval().getType()) {
                new RuntimeError("Argument " + arg + " of function " + name + " must be of type " + parameters.get(arg) + ", " + args.get(i) + " given", fileName, line, pos);
            }
            i++;
        }
        function.eval(args);
    }
}
