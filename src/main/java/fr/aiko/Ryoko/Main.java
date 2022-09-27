package fr.aiko.Ryoko;

import fr.aiko.Ryoko.parser.Lexer;
import fr.aiko.Ryoko.parser.Token;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("klm");
        ArrayList<Token> tokens = lexer.tokens;

        for (Token token : tokens) {
            System.out.println(token.getType() + " " + token.getValue());
        }
    }
}