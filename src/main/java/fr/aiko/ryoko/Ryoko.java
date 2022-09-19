package fr.aiko.ryoko;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ryoko {
    public static void main(String[] args) {
        try {
            CharStream stream = CharStreams.fromStream(Ryoko.class.getClassLoader().getResourceAsStream("test.ry")); // CharStreams.fromPath(Paths.get("test.ry"));
            RyokoLexer lexer = new RyokoLexer(stream);
            RyokoParser parser = new RyokoParser(new CommonTokenStream(lexer));
            parser.setBuildParseTree(true);
            parser.addParseListener(new RyokoCustomListener());
            ParseTree tree = parser.program();
        } catch (IOException ex) {
            Logger.getLogger(Ryoko.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
