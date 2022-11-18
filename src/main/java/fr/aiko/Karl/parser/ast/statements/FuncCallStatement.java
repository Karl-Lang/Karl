package fr.aiko.Karl.parser.ast.statements;

import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.std.FunctionManager;

import java.util.HashMap;

public class FuncCallStatement extends Statement {
    private final String name;
    private final HashMap<String, TokenType> args;

    public FuncCallStatement(String name) { // HashMap<String, TokenType> args
        this.name = name;
        this.args = null;
    }

    @Override
    public void eval() {
        FunctionManager.getFunction(name).eval();
    }
}
