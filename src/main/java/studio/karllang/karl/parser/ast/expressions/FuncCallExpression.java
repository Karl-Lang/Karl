package studio.karllang.karl.parser.ast.expressions;

import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.lexer.TokenType;
import studio.karllang.karl.parser.ast.values.NullValue;
import studio.karllang.karl.parser.ast.values.Value;
import studio.karllang.karl.std.Function;
import studio.karllang.karl.std.FunctionManager;
import studio.karllang.karl.std.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FuncCallExpression extends Expression {
    private final String name;
    private final ArrayList<Expression> args;
    private final int line;
    private final int pos;

    public FuncCallExpression(String name, ArrayList<Expression> args, int line, int pos) {
        this.name = name;
        this.args = args;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public Value eval() throws RuntimeError {
        if (!FunctionManager.isFunction(name)) {
            throw new RuntimeError("Unknown function: " + name, pos, line, printString());
        }

        Function function = FunctionManager.getFunction(name);
        LinkedHashMap<String, TokenType> parameters = function.getArgs();
        if (args.size() != parameters.size()) {
            throw new RuntimeError("Function " + name + " takes " + parameters.size() + " arguments, " + args.size() + " given", pos, line, printString());
        }

        int i = 0;
        for (String arg : parameters.keySet()) {
            if (!Types.checkValueType(parameters.get(arg), args.get(i).eval().getType())) {
                throw new RuntimeError("Type mismatch for argument " + arg + " of function " + name + ": Excepted type " + Types.getTypeName(parameters.get(arg)) + ", but got type " + Types.getTypeName(args.get(i).eval().getType()), pos, line, printString());
            }
            i++;
        }

        if (function.getType() == TokenType.VOID) {
            function.eval(args, line, pos);
            return new NullValue("null_void");
        } else return function.eval(args, line, pos);
    }

    public String printString() throws RuntimeError {
        StringBuilder optionString = new StringBuilder();
        for (Expression arg : args) {
            optionString.append((optionString.length() == 0) ? arg.eval().toString() : arg.eval().toString() + ", ");
        }
        return name + "(" + optionString + ");";
    }
}
