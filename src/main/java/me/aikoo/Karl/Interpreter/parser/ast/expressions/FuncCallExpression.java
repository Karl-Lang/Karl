package me.aikoo.Karl.Interpreter.parser.ast.expressions;

import me.aikoo.Karl.Interpreter.errors.RuntimeError.RuntimeError;
import me.aikoo.Karl.Interpreter.parser.TokenType;
import me.aikoo.Karl.Interpreter.parser.ast.values.NullValue;
import me.aikoo.Karl.Interpreter.parser.ast.values.Value;
import me.aikoo.Karl.Interpreter.std.Function;
import me.aikoo.Karl.Interpreter.std.FunctionManager;
import me.aikoo.Karl.Interpreter.std.Types;

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
        if (!FunctionManager.getCurrentFile().isFunction(name)) {
            new RuntimeError("Unknown function: " + name, fileName, line, pos);
        }

        Function function = FunctionManager.getCurrentFile().getFunction(name);
        LinkedHashMap<String, TokenType> parameters = function.getArgs();
        if (args.size() != parameters.size()) {
            new RuntimeError("Function " + name + " takes " + parameters.size() + " arguments, " + args.size() + " given", fileName, line, pos);
        }

        int i = 0;
        for (String arg : parameters.keySet()) {
            if (!Types.checkValueType(parameters.get(arg), args.get(i).eval().getType())) {
                new RuntimeError("Type mismatch for argument " + arg + " of function " + name + ": Excepted type " + Types.getTypeName(parameters.get(arg)) + ", but got type " + Types.getTypeName(args.get(i).eval().getType()), fileName, line, pos);
            }
            i++;
        }

        if (function.getType() == TokenType.VOID) {
            function.eval(args, fileName, line, pos);
            return new NullValue("null_void");
        } else return function.eval(args, fileName, line, pos);
    }
}
