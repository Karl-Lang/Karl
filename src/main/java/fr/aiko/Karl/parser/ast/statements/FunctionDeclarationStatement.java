package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.std.Function;
import fr.aiko.Karl.std.FunctionManager;

import java.util.HashMap;

public class FunctionDeclarationStatement extends Statement {
    private final String name;
    private final HashMap<String, TokenType> args;
    private final BlockStatement body;
    private final TokenType type;

    public FunctionDeclarationStatement(String name, HashMap<String, TokenType> args, TokenType returnType, BlockStatement block) {
        this.name = name;
        this.args = args;
        this.body = block;
        this.type = returnType;
    }

    @Override
    public void eval() {}
}
