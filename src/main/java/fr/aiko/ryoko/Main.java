package fr.aiko.ryoko;

import fr.aiko.RyokoLexer;
import fr.aiko.RyokoParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
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
            long start = System.currentTimeMillis();
            String path = "/home/aikoo/Bureau/Developpement/Projets/Ryoko/src/main/resources/test.ry";// args[0];
            CharStream stream = CharStreams.fromPath(Paths.get(path));
            RyokoLexer lexer = new RyokoLexer(stream);
            RyokoParser parser = new RyokoParser(new CommonTokenStream(lexer));
            parser.setBuildParseTree(true);

            RyokoCustomVisitor visitor = new RyokoCustomVisitor();
            visitor.visit(parser.program()); // Execution du code
            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Execution time : " + timeElapsed + "ms");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
