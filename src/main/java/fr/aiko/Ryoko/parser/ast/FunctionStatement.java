package fr.aiko.Ryoko.parser.ast;

import fr.aiko.Ryoko.parser.Parser;
import fr.aiko.Ryoko.parser.Token;

import javax.swing.plaf.nimbus.State;
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
        System.out.println(parser.VARIABLE_MAP.get("name").getValue());
        ArrayList<Statement> statements = parser.parse();
        for (Statement statement : statements) {
            statement.execute();
        }
    }
}
