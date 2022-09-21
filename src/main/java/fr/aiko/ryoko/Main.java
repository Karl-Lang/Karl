package fr.aiko.ryoko;

import fr.aiko.RyokoLexer;
import fr.aiko.RyokoParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        try {
            execute(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void execute(String[] args) {
        try {
            String path = args[0];
            CharStream stream = CharStreams.fromPath(Paths.get(path)); // Main.class.getClassLoader().getResourceAsStream("test.ry")
            RyokoLexer lexer = new RyokoLexer(stream);
            RyokoParser parser = new RyokoParser(new CommonTokenStream(lexer));
            parser.setBuildParseTree(true);

            RyokoCustomVisitor visitor = new RyokoCustomVisitor();
            visitor.visit(parser.program());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
