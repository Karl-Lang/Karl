package me.aikoo.Karl.Interpreter;

import me.aikoo.Karl.Interpreter.errors.FileError.FileError;
import me.aikoo.Karl.Interpreter.errors.FileError.FileNotFoundError;
import me.aikoo.Karl.Interpreter.parser.Lexer;
import me.aikoo.Karl.Interpreter.parser.Parser;
import me.aikoo.Karl.Interpreter.parser.Token;
import me.aikoo.Karl.Interpreter.parser.ast.statements.Statement;
import me.aikoo.Karl.Interpreter.std.FunctionManager;
import me.aikoo.Karl.Interpreter.std.VariableManager;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@Command(name = "karl", mixinStandardHelpOptions = true, version = "karl 0.4.0-alpha.5", description = "Karl programming language")
public class Main implements Runnable {
    @CommandLine.Parameters(index = "0", description = "The file to run")
    private String path;

    @CommandLine.Option(names = {"-e", "--exec", "--execution"}, description = "Execution time")
    private String isEnabled;

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    @Override
    public void run() {
        if (!Files.exists(Path.of(path))) {
            new FileNotFoundError(path);
        }
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        if (!fileName.endsWith(".karl")) {
            new FileError(path);
        }

        try {
            VariableManager.addFile(fileName);
            FunctionManager.addFile(fileName);

            ArrayList<Token> tokens = new Lexer(Files.readString(Path.of(path)), path).tokens;
            ArrayList<Statement> statements = new Parser(tokens, path).parse();

            Long start = System.currentTimeMillis();
            statements.forEach(Statement::eval);
            Long end = System.currentTimeMillis();

            VariableManager.clear();
            FunctionManager.clear();

            if (Boolean.parseBoolean(isEnabled)) {
                long elapsedTime = end - start;
                System.out.println("Execution time: " + elapsedTime + "ms");
            }
        } catch (IOException e) {
            new FileError(path);
        }

        /*try {
            String[] array = new String[100000 * 100000];
        } catch (OutOfMemoryError e) {
            System.out.println("Out of memory uwu baka");
        }*/
    }
}