package fr.aiko.Karl;

import fr.aiko.Karl.parser.Lexer;
import fr.aiko.Karl.parser.Token;
import fr.aiko.Karl.parser.ast.Parser;
import fr.aiko.Karl.parser.ast.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "/home/aikoo/Bureau/Developpement/Projets/Karl/src/main/resources/Main.ry";
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        Lexer lexer = new Lexer(Files.readString(Path.of(path)), fileName);
        ArrayList<Token> tokens = lexer.tokens;

        Parser parser = new Parser(tokens, fileName);
        ArrayList<Statement> statements = parser.parse();

        for (Statement statement : statements) {
            statement.execute();
        }
    }
}