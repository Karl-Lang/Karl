package fr.aiko.Ryoko.parser;

import fr.aiko.Ryoko.parser.ast.PrintStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private ArrayList<Token> tokens;
    private final ArrayList<Statement> statements = new ArrayList<>();
    private Token currentToken;
    private final Map<TokenType, String> FUNC_CALL = new HashMap<>();

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        currentToken = tokens.get(0);

        FUNC_CALL.put(TokenType.PRINT, "SYSTEM_SHOW");
    }

    public ArrayList<Statement> parse() {
        while (tokens.indexOf(currentToken) < tokens.size()) {

            if (currentToken.getType() == TokenType.EOF) {
                break;
            }

            Statement statement = statement();
            this.statements.add(statement);

            if (tokens.indexOf(currentToken) + 1 < tokens.size()) {
                advance();
            }
        }
        return this.statements;
    }

    private Statement statement() {
        if (FUNC_CALL.containsKey(currentToken.getType())) {
            ArrayList<Token> args = getFuncCallArguments(currentToken);
            if (tokens.get(tokens.indexOf(currentToken)).getType() != TokenType.SEMICOLON) {
                throw new RuntimeException("Missing ; at the end of the print statement.\nLine: " + currentToken.getLine());
            }

            StringBuilder contentToPrint = new StringBuilder();

            for (Token arg : args) {
                contentToPrint.append(arg.getValue());
            }

            return new PrintStatement(contentToPrint.toString());
        } else {
            throw new RuntimeException("Invalid token : " + currentToken.getValue());
        }
    }

    private ArrayList<Token> getFuncCallArguments(Token currentTok) {
        advance();
        advance();
        ArrayList<Token> tokensToReturn = new ArrayList<>();
        while (currentToken.getType() != TokenType.RIGHT_PARENTHESIS &&  tokens.indexOf(currentToken) + 1 < tokens.size()) {
            tokensToReturn.add(currentToken);
            advance();
        }
        advance();

        return tokensToReturn;
    }

    private void advance() {
        currentToken = tokens.get(tokens.indexOf(currentToken) + 1);
    }
}