package fr.aiko.Karl.parser.ast.expressions;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.values.NullValue;
import fr.aiko.Karl.parser.ast.values.Value;
import fr.aiko.Karl.std.Function;
import fr.aiko.Karl.std.FunctionManager;
import fr.aiko.Karl.std.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FuncCallExpression extends Expression {
    private final String name;
    private final ArrayList<Expression> args;
    private final String fileName;
    private final int line;
    private final int pos;

    public FuncCallExpression(String name, ArrayList<Expression> args, String fileName, int line, int pos) {
        this.name = name;
        this.args = args;
        this.fileName = fileName;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public Value eval() {
        if (!FunctionManager.isFunction(name)) {
            new RuntimeError("Unknown function: " + name, fileName, line, pos);
        }

        Function function = FunctionManager.getFunction(name);
        LinkedHashMap<String, TokenType> parameters = function.getArgs();
        if (args.size() != parameters.size()) {
            new RuntimeError("Function " + name + " takes " + parameters.size() + " arguments, " + args.size() + " given", fileName, line, pos);
        }

        int i = 0;
        for (String arg : parameters.keySet()) {
            if (!Types.checkValueType(parameters.get(arg), args.get(i).eval().getType())) { // parameters.get(arg) != args.get(i).eval().getType()
                new RuntimeError("Type mismatch for argument " + arg + " of function " + name + ": Excepted type " + parameters.get(arg).getName() + ", but got type " + args.get(i).eval().getType().getName(), fileName, line, pos);
            }
            i++;
        }

        if (function.getType() == TokenType.VOID) {
            function.eval(args, fileName, line, pos);
            return new NullValue("null");
        } else return function.eval(args, fileName, line, pos);
    }
}
