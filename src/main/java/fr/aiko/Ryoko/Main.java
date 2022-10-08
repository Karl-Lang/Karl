package fr.aiko.Ryoko;

import fr.aiko.Ryoko.parser.Lexer;
import fr.aiko.Ryoko.parser.Parser;
import fr.aiko.Ryoko.parser.Statement;
import fr.aiko.Ryoko.parser.Token;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("print(\"RAAAH ENFIN\");");
        ArrayList<Token> tokens = lexer.tokens;

        Parser parser = new Parser(tokens);
        ArrayList<Statement> statements = parser.parse();

        for (Statement statement : statements) {
            statement.execute();
        }
    }
}