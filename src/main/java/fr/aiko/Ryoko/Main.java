package fr.aiko.Ryoko;

import fr.aiko.Ryoko.parser.Lexer;
import fr.aiko.Ryoko.parser.Parser;
import fr.aiko.Ryoko.parser.Token;
import fr.aiko.Ryoko.parser.ast.Statement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "/home/aikoo/Bureau/Developpement/Projets/RyokoHomeMade/src/main/resources/Main.ry";
        Lexer lexer = new Lexer(Files.readString(Path.of(path)));
        ArrayList<Token> tokens = lexer.tokens;

        Parser parser = new Parser(tokens);
        ArrayList<Statement> statements = parser.parse();

        for (Statement statement : statements) {
            statement.execute();
        }
    }
}