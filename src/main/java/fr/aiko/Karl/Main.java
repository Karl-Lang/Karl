package fr.aiko.Karl;

import fr.aiko.Karl.parser.Lexer;
import fr.aiko.Karl.parser.Parser;
import fr.aiko.Karl.parser.Token;
import fr.aiko.Karl.parser.TokenType;
import fr.aiko.Karl.parser.ast.IfStatement;
import fr.aiko.Karl.parser.ast.Statement;
import fr.aiko.Karl.ErrorManager.Error;
import fr.aiko.Karl.parser.ast.Variable;
import fr.aiko.Karl.parser.ast.VariableAssignmentStatement;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

@Command(name = "karl", mixinStandardHelpOptions = true, version = "karl 0.2.0", description = "Karl programming language")
public class Main implements Runnable {
    @CommandLine.Parameters(index = "0", description = "The file to run")
    private String path;

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    @Override
    public void run() {
        if (!Files.exists(Path.of(path))) {
            new Error("FileNotFound", "The file " + path + " doesn't exist", "Main.java", 0);
        }
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        if (!fileName.endsWith(".karl")) {
            new Error("FileError", "The file must be a .karl file", fileName, 0);
        }

        Long start = System.currentTimeMillis();
        Lexer lexer = null;
        try {
            lexer = new Lexer(Files.readString(Path.of(path)), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Token> tokens = lexer.tokens;
        execute(new Parser(tokens, fileName));
        Long end = System.currentTimeMillis();
        System.out.println("Execution time: " + (end - start) + "ms");
    }

    public void execute(Parser parser) {
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
                execute(newParser);
                break;
            } else if (statement instanceof IfStatement ifStatement) {
                Parser ifParser = ifStatement.getParser();

                if (ifParser != null) {
                    Map<String, Variable> variableMap = ifParser.getVariables();
                    ArrayList<Token> ifTokens = ifStatement.getTokens();

                    for (Variable variable : variableMap.values()) {
                        if (parser.getVariables().containsKey(variable.getName())) {
                            parser.getVariables().get(variable.getName()).setValue(variable.getValue());
                        }
                    }

                    Token lastToken = ifTokens.get(ifTokens.size() - 2);
                    tokens.subList(0, tokens.indexOf(lastToken) + 2).clear();

                    if (tokens.get(0).getType() != TokenType.ELSE) {
                        Parser newParser = new Parser(tokens, parser.getFileName());
                        newParser.setVariables(parser.getVariables());
                        execute(newParser);
                        break;
                    } else {
                        Parser elseParser = ifStatement.getElseParser();
                        Token lastElseToken = elseParser.getTokens().get(elseParser.getTokens().size() - 2);
                        tokens.subList(0, tokens.indexOf(lastElseToken) + 2).clear();
                        Parser newParser = new Parser(tokens, parser.getFileName());
                        newParser.setVariables(parser.getVariables());
                        execute(newParser);
                        break;
                    }
                }
            }
        }
    }
}