package fr.aiko.Karl.parser.ast;

import fr.aiko.Karl.parser.Parser;
import fr.aiko.Karl.parser.Token;

import java.util.ArrayList;
import java.util.Map;

public class IfStatement extends Statement {
    private final Parser parser;
    private final Parser ifParser;
    private final Parser elseParser;
    private final ArrayList<Token> baseTokens = new ArrayList<>();
    private final boolean condition;

    public IfStatement(boolean condition, Parser ifParser, Parser elseParser) {
        this.condition = condition;
        this.ifParser = ifParser;
        this.elseParser = elseParser;
        this.parser = condition ? ifParser : elseParser;
        if (this.parser != null) {
            this.baseTokens.addAll(this.parser.getTokens());
        }
    }

    @Override
    public void execute() {
        if (this.parser != null) {
            executeCondition(parser);
        }
    }

    public void executeCondition(Parser parser) {
        ArrayList<Statement> statements = parser.parse();
        ArrayList<Token> tokens = parser.getTokens();

        for (Statement statement : statements) {
            statement.execute();

            if (statement instanceof VariableAssignmentStatement var) {
                Map<String, Variable> refreshedMap = var.refreshVariables();

                for (Variable variable : refreshedMap.values()) {
                    if (parser.getVariables().containsKey(variable.getName())) {
                        parser.getVariables().get(variable.getName()).setValue(variable.getValue());
                    }
                }

                tokens.subList(0, tokens.indexOf(var.getToken()) + 1).clear();
                Parser newParser = new Parser(tokens, parser.getFileName());
                newParser.setVariables(parser.getVariables());
                executeCondition(newParser);
                break;
            }
        }
    }

    public Parser getParser() {
        return this.parser;
    }

    public ArrayList<Token> getTokens() {
        return this.baseTokens;
    }

    public Parser getIfParser() {
        return this.ifParser;
    }

    public Parser getElseParser() {
        return this.elseParser;
    }
}
