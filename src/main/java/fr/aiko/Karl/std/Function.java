package fr.aiko.Karl.std;

import fr.aiko.Karl.errors.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.expressions.Expression;
import fr.aiko.Karl.parser.ast.statements.BlockStatement;
import fr.aiko.Karl.parser.ast.statements.Statement;
import fr.aiko.Karl.parser.ast.values.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Function {
    private final String name;
    private final LinkedHashMap<String, TokenType> args;
    private final BlockStatement body;
    private final TokenType type;
    private ArrayList<Expression> argsValues;

    public Function(String name, LinkedHashMap<String, TokenType> args, TokenType returnType, BlockStatement body) {
        this.name = name;
        this.args = args;
        this.body = body;
        this.type = returnType;
    }

    public Value eval(ArrayList<Expression> values, String fileName, int line, int pos) {
        this.argsValues = values;
        // For each variable in args, assign new value
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
                new RuntimeError("Function " + name + " is void, but return a value", fileName, line, pos);
            }
            if (body.getResult().getType() == type) {
                return body.getResult();
            } else {
                new RuntimeError("Function " + name + " returns type " + type.getName() + ", but got type " + body.getResult().getType().getName(), fileName, line, pos);
            }
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
}
