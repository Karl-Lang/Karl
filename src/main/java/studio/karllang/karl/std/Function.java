package studio.karllang.karl.std;

import studio.karllang.karl.errors.runtime.RuntimeError;
import studio.karllang.karl.olderrors.runtime.RuntimeOldError;
import studio.karllang.karl.lexer.TokenType;
import studio.karllang.karl.parser.ast.expressions.Expression;
import studio.karllang.karl.parser.ast.statements.BlockStatement;
import studio.karllang.karl.parser.ast.statements.Statement;
import studio.karllang.karl.parser.ast.values.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Function {
    private final String name;
    private final LinkedHashMap<String, TokenType> args;
    private final BlockStatement body;
    private final TokenType type;

    public Function(String name, LinkedHashMap<String, TokenType> args, TokenType returnType, BlockStatement body) {
        this.name = name;
        this.args = args;
        this.body = body;
        this.type = returnType;
    }

    public Value eval(ArrayList<Expression> values, String fileName, int line, int pos) throws RuntimeError {
        HashMap<String, Value> arguments = new HashMap<>();
        int i = 0;
        for (String arg : args.keySet()) {
            arguments.put(arg, values.get(i).eval());
            i++;
        }
        body.setArgs(arguments);
        body.eval();
        if (body.getResult() != null) {
            if (type == TokenType.VOID) {
                throw new RuntimeError("Function " + name + " is void, but return a value", pos, line, printString());
            }
            if (Types.checkValueType(type, body.getResult().getType()) || (type == TokenType.STRING && body.getResult().getType() == TokenType.NULL)) {
                return body.getResult();
            } else {
                throw new RuntimeError("Incorrect return type for function " + name + ": except " + type.getName() + " but got type " + body.getResult().getType().getName(), pos, line, printString());
            }
        } else if (body.getResult() == null && type != TokenType.VOID) {
            throw new RuntimeError("Missing return statement in function: " + name, pos, line, printString());
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public LinkedHashMap<String, TokenType> getArgs() {
        return args;
    }

    public Statement getBody() {
        return body;
    }

    public TokenType getType() {
        return type;
    }

    public String printString() throws RuntimeError {
        StringBuilder optionString = new StringBuilder();
        for (String key : args.keySet()) {
            optionString.append((optionString.length() == 0) ? key : key + ", ");
        }
        return name + "(" + optionString + ");";
    }
}
