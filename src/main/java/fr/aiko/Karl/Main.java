package fr.aiko.Karl;

import fr.aiko.Karl.parser.Lexer;
import fr.aiko.Karl.parser.Parser;
import fr.aiko.Karl.parser.Token;
import fr.aiko.Karl.parser.ast.Statement;
import fr.aiko.Karl.ErrorManager.Error;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@Command(name = "karl", mixinStandardHelpOptions = true, version = "karl 0.2.0", description = "Karl programming language")
public class Main implements Runnable {
    @CommandLine.Parameters(index = "0", description = "The file to run")
    private String path;

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    @Override
    public void run() {
        // Check if the file exists
        if (!Files.exists(Path.of(path))) {
            new Error("FileNotFound", "The file " + path + " doesn't exist", "Main.java", 0);
        }
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        if (!fileName.endsWith(".karl")) {
            new Error("FileError", "The file must be a .karl file", fileName, 0);
        }

        Lexer lexer = null;
        try {
            lexer = new Lexer(Files.readString(Path.of(path)), fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Token> tokens = lexer.tokens;

        Parser parser = new Parser(tokens, fileName);
        ArrayList<Statement> statements = parser.parse();

        for (Statement statement : statements) {
            statement.execute();
        }
    }
}