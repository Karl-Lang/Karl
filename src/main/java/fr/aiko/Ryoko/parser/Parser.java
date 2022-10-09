package fr.aiko.Ryoko.parser;

import fr.aiko.Ryoko.parser.ast.PrintStatement;
import fr.aiko.Ryoko.parser.ast.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private final ArrayList<Token> tokens;
    private final ArrayList<Statement> statements = new ArrayList<>();
    private final Map<TokenType, String> FUNC_CALL = new HashMap<>();
    private final Map<String, Object> VARIABLE_MAP = new HashMap<>();
    private Token currentToken;

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
            ArrayList<Token> args = getFuncCallArguments();
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
            String type = currentToken.getValue();
            advance();
            advance();
            String varName = currentToken.getValue();
            advance();
            advance();
            if (checkCorrespondentTypeVariable(type, currentToken)) {
                VARIABLE_MAP.put(varName, currentToken.getValue());
            } else {
                throw new RuntimeException("The variable " + varName + " is not of type " + type + ".\nLine: " + currentToken.getLine());
            }
            advance();
            if (currentToken.getType() != TokenType.SEMICOLON) {
                throw new RuntimeException("Missing ; at the end of the variable declaration.\nLine: " + currentToken.getLine());
            }
            advance();
            return true;
        } else if (currentToken.getType() == TokenType.DIVIDE && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.DIVIDE) {
            int pos = currentToken.getLine();
            while (currentToken.getLine() == pos) {
                if (tokens.indexOf(currentToken) + 1 < tokens.size()) {
                    advance();
                } else break;
            }
            return true;
        } else return false;
    }

    private ArrayList<Token> getFuncCallArguments() {
        advance();
        advance();
        ArrayList<Token> tokensToReturn = new ArrayList<>();
        while (currentToken.getType() != TokenType.RIGHT_PARENTHESIS && tokens.indexOf(currentToken) + 1 < tokens.size()) {
            if (currentToken.getType() == TokenType.COMMA) {
                advance();
                continue;
            }

            tokensToReturn.add(currentToken);
            advance();
        }
        advance();

        return tokensToReturn;
    }

    private boolean isVariableDeclaration() {
        String[] types = {"int", "float", "string", "bool"};
        if (Arrays.asList(types).contains(currentToken.getValue()) && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 3).getType() == TokenType.EQUALS) {
            return true;
        } else {
            if (Arrays.asList(types).contains(tokens.get(tokens.indexOf(currentToken) + 2).getValue())) {
                throw new RuntimeException("Variable name can't be a type\nLine: " + currentToken.getLine());
            }
            return false;
        }
    }

    private boolean checkCorrespondentTypeVariable(String type, Token value) {
        if (value.getType() == TokenType.IDENTIFIER) {
            if (VARIABLE_MAP.containsKey(value.getValue())) {
                return VARIABLE_MAP.get(value.getValue()).getClass().getSimpleName().toLowerCase().equals(type);
            } else {
                throw new RuntimeException("Unknown variable: " + value.getValue() + ".\nLine: " + value.getLine());
            }
        } else {
            return switch (type) {
                case "int" -> value.getType() == TokenType.INT;
                case "float" -> value.getType() == TokenType.FLOAT;
                case "string" -> value.getType() == TokenType.STRING;
                case "bool" -> value.getType() == TokenType.BOOL;
                default -> false;
            };
        }
    }

    private void advance() {
        currentToken = tokens.get(tokens.indexOf(currentToken) + 1);
    }
}