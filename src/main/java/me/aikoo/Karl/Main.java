package me.aikoo.Karl;

import me.aikoo.Karl.errors.FileError.FileError;
import me.aikoo.Karl.errors.FileError.FileNotFoundError;
import me.aikoo.Karl.parser.Lexer;
import me.aikoo.Karl.parser.Parser;
import me.aikoo.Karl.parser.Token;
import me.aikoo.Karl.parser.ast.statements.Statement;
import me.aikoo.Karl.std.FunctionManager;
import me.aikoo.Karl.std.VariableManager;
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
            Lexer lexer = new Lexer(Files.readString(Path.of(path)), path);
            ArrayList<Token> tokens = lexer.tokens;
            Parser parser = new Parser(tokens, path);
            VariableManager.addFile(fileName);
            ArrayList<Statement> statements = parser.parse();
            Long start = System.currentTimeMillis();
            statements.forEach(Statement::eval);
            Long end = System.currentTimeMillis();
            VariableManager.getCurrentFile().clear();
            FunctionManager.clear();

            if (Boolean.parseBoolean(isEnabled)) {
                System.out.println("Execution time: " + (end - start) + "ms");
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