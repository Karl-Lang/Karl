package fr.aiko.Ryoko.parser;

import fr.aiko.Ryoko.parser.ast.PrintStatement;
import fr.aiko.Ryoko.parser.ast.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private ArrayList<Token> tokens;
    private final ArrayList<Statement> statements = new ArrayList<>();
    private Token currentToken;
    private final Map<TokenType, String> FUNC_CALL = new HashMap<>();
    private final Map<String, Object> VARIABLE_MAP = new HashMap<>();

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

            if (statement != null) {
                this.statements.add(statement);
            } else {
                boolean expr = expression();
                if (!expr) {
                    throw new RuntimeException("Unknown statement : " + currentToken.getValue() + "\nLine: " + currentToken.getLine());
                } else continue;
            }

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
                if (arg.getType() == TokenType.IDENTIFIER) {
                    if (VARIABLE_MAP.containsKey(arg.getValue())) {
                        contentToPrint.append(VARIABLE_MAP.get(arg.getValue()));
                    } else {
                        throw new RuntimeException("Unknown variable " + arg.getValue() + ".\nLine: " + arg.getLine());
                    }
                } else {
                    contentToPrint.append(arg.getValue());
                }
            }

            return new PrintStatement(contentToPrint.toString());
        } else return null;
    }

    public boolean expression() {
        if (isVariableDeclaration()) {
            advance();
            advance();
            String varName = currentToken.getValue();
            advance();
            advance();
            VARIABLE_MAP.put(varName, currentToken.getValue());
            advance();
            if (currentToken.getType() != TokenType.SEMICOLON) {
                throw new RuntimeException("Missing ; at the end of the variable declaration.\nLine: " + currentToken.getLine());
            }
            advance();
            return true;
        } else return false;
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

    private boolean isVariableDeclaration() {
        // return currentToken.getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.EQUALS;
        String[] types = {"int", "float", "string", "bool"};
        // Contains the type of the variable
        return Arrays.asList(types).contains(currentToken.getValue()) && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 3).getType() == TokenType.EQUALS;
    }

    private void advance() {
        currentToken = tokens.get(tokens.indexOf(currentToken) + 1);
    }
}