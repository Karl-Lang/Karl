package fr.aiko.Karl.std;

import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.expressions.Expression;
import fr.aiko.Karl.parser.ast.statements.BlockStatement;
import fr.aiko.Karl.parser.ast.statements.Statement;
import fr.aiko.Karl.parser.ast.values.Value;

import java.util.ArrayList;
import java.util.HashMap;

public class Function {
    private final String name;
    private final HashMap<String, TokenType> args;
    private final BlockStatement body;
    private final TokenType type;
    private ArrayList<Expression> argsValues;

    public Function(String name, HashMap<String, TokenType> args, TokenType returnType, BlockStatement body) {
        this.name = name;
        this.args = args;
        this.body = body;
        this.type = returnType;
    }

    public void eval(ArrayList<Expression> values) {
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
    }

    public String getName() {
        return name;
    }

    public HashMap<String, TokenType> getArgs() {
        return args;
    }

    public Statement getBody() {
        return body;
    }

    public TokenType getType() {
        return type;
    }
}
