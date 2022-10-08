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
            String func = FUNC_CALL.get(currentToken.getType());
            ArrayList<Token> args = getFuncCallArguments(currentToken);
            String contentToPrint = "";

            for (Token arg : args) {
                contentToPrint += arg.getValue();
            }

            return new PrintStatement(contentToPrint);
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