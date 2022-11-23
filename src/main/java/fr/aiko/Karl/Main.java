package fr.aiko.Karl;

import fr.aiko.Karl.errors.Error;
import fr.aiko.Karl.parser.Lexer;
import fr.aiko.Karl.parser.Parser;
import fr.aiko.Karl.parser.Token;
import fr.aiko.Karl.parser.ast.statements.Statement;
import fr.aiko.Karl.std.FunctionManager;
import fr.aiko.Karl.std.VariableManager;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@Command(name = "karl", mixinStandardHelpOptions = true, version = "karl 0.4.0-alpha", description = "Karl programming language")
public class Main implements Runnable {
    @CommandLine.Parameters(index = "0", description = "The file to run")
    private String path;

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    @Override
    public void run() {
        if (!Files.exists(Path.of(path))) {
            new Error("FileNotFound", "The file " + path + " doesn't exist", "Main.java", 0, 0);
        }
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        if (!fileName.endsWith(".karl")) {
            new Error("FileError", "The file must be a .karl file", fileName, 0, 0);
        }

        try {
            Lexer lexer = new Lexer(Files.readString(Path.of(path)), path);
            ArrayList<Token> tokens = lexer.tokens;
            Parser parser = new Parser(tokens, path);
            ArrayList<Statement> statements = parser.parse();

            Long start = System.currentTimeMillis();
            statements.forEach(Statement::eval);
            Long end = System.currentTimeMillis();
            VariableManager.clear();
            FunctionManager.clear();
            System.out.println("Execution time: " + (end - start) + "ms");
        } catch (IOException e) {
            new Error("FileReading", "An error occured while reading file : " + path, "Main.java", 0, 0);
        }

        /*try {
            String[] array = new String[100000 * 100000];
        } catch (OutOfMemoryError e) {
            System.out.println("Out of memory uwu baka");
        }*/
    }
}