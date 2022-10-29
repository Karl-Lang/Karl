package fr.aiko.Karl.parser.ast;

import fr.aiko.Karl.ErrorManager.RuntimeError.RuntimeError;
import fr.aiko.Karl.parser.Parser;

import java.util.ArrayList;

public class FunctionStatement extends Statement {
    public String name;
    public Parser parser;
    public ArrayList<Variable> args;
    public int argsNumber;

    public FunctionStatement(String name, ArrayList<Variable> args, Parser parser) {
        this.name = name;
        this.argsNumber = args.size();
        this.args = args;
        this.parser = parser;
    }

    @Override
    public void execute() {
        ArrayList<Statement> statements = parser.parse();
        for (Statement statement : statements) {
            statement.execute();
        }
    }
}
