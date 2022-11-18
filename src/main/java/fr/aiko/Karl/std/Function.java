package fr.aiko.Karl.std;

import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.statements.BlockStatement;
import fr.aiko.Karl.parser.ast.statements.Statement;

import java.util.HashMap;

public class Function {
    private final String name;
    private final HashMap<String, TokenType> args;
    private final Statement body;
    private final TokenType type;

    public Function(String name, HashMap<String, TokenType> args, TokenType returnType, BlockStatement body) {
        this.name = name;
        this.args = args;
        this.body = body;
        this.type = returnType;
    }

    public void eval() {
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
