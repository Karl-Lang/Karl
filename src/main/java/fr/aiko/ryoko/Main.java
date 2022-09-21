package fr.aiko.ryoko;

import fr.aiko.RyokoLexer;
import fr.aiko.RyokoParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        try {
            execute(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Object execute(String[] args) {
        try {
            CharStream stream = CharStreams.fromStream(Main.class.getClassLoader().getResourceAsStream("test.ry")); // CharStreams.fromPath(Paths.get("test.ry"));
            RyokoLexer lexer = new RyokoLexer(stream);
            RyokoParser parser = new RyokoParser(new CommonTokenStream(lexer));
            parser.setBuildParseTree(true);

            RyokoCustomVisitor visitor = new RyokoCustomVisitor();
            return visitor.visit(parser.program());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
