package fr.aiko.Ryoko.parser;

import fr.aiko.Ryoko.parser.ErrorManager.SemiColonException;
import fr.aiko.Ryoko.parser.ErrorManager.TypeException;
import fr.aiko.Ryoko.parser.ErrorManager.UnknownVariableException;
import fr.aiko.Ryoko.parser.ErrorManager.VariableNameException;
import fr.aiko.Ryoko.parser.ast.FunctionStatement;
import fr.aiko.Ryoko.parser.ast.PrintStatement;
import fr.aiko.Ryoko.parser.ast.Statement;
import fr.aiko.Ryoko.parser.ast.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private final ArrayList<Token> tokens;
    private final ArrayList<Statement> statements = new ArrayList<>();
    private final Map<TokenType, String> FUNC_CALL = new HashMap<>();
    public final Map<String, Variable> VARIABLE_MAP = new HashMap<>();
    private final Map<String, FunctionStatement> FUNCTIONS = new HashMap<>();
    private final String[] types = {"int", "float", "string", "bool"};
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
                        contentToPrint.append(VARIABLE_MAP.get(arg.getValue()).getValue());
                    } else {
                        throw new UnknownVariableException(arg.getValue(), arg.getLine(), arg.getStart());
                    }
                } else {
                    contentToPrint.append(arg.getValue());
                }
            }

            return new PrintStatement(contentToPrint.toString());
        }  else if (isFuncCall()) {
            String funcName = currentToken.getValue();
            if (!FUNCTIONS.containsKey(funcName)) {
                throw new RuntimeException("Unknown function : " + funcName + "\nLine: " + currentToken.getLine());
            }

            ArrayList<Token> args = getFuncCallArguments();
            FunctionStatement function = FUNCTIONS.get(funcName);

            if (args.size() != function.argsNumber) {
                throw new RuntimeException("Wrong number of arguments for function " + funcName + ". Expected " + function.argsNumber + " but got " + args.size() + "\nLine: " + currentToken.getLine());
            }

            function.parser.VARIABLE_MAP.forEach((key, value) -> {
                for (int i = 0; i < args.size(); i++) {
                    Token token = args.get(i); // TODO: Check if types is correspondant
                    String varType = function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).getType();

                    if (token.getType().toString().toLowerCase().equals(varType) || token.getType() == TokenType.IDENTIFIER) {
                        if (token.getType() == TokenType.IDENTIFIER) {
                            if (VARIABLE_MAP.containsKey(token.getValue())) {
                                // function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).setValue(VARIABLE_MAP.get(token.getValue()).getValue());
                                if (VARIABLE_MAP.get(token.getValue()).getType().equals(varType)) {
                                    function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).setValue(VARIABLE_MAP.get(token.getValue()).getValue());
                                } else {
                                    throw new TypeException("Excepted type " + varType + " for argument : " + function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).getName() + " in function "  + funcName + ".\nThe entered value type is : " + VARIABLE_MAP.get(token.getValue()).getType(), token.getLine(), token.getStart());
                                }
                            } else {
                                throw new UnknownVariableException(token.getValue(), token.getLine(), token.getStart());
                            }
                        } else {
                            function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).setValue(token.getValue());
                        }
                    } else {
                        throw new TypeException("Excepted type " + varType + " for argument " + function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).getName() + " in function "  + funcName + ".\nThe entered value type is : " + token.getType().toString(), token.getLine(), token.getStart());
                    }
                }
            });

            return function;
        } else return null;
    }

    public boolean expression() {
        if (isVariableDeclaration()) {
            boolean isFinal = isFinalVariableDeclaration();
            if (isFinal) advance();
            String type = currentToken.getValue();
            advance(2);
            String varName = currentToken.getValue();
            advance(2);
            if (checkCorrespondentTypeVariable(type, currentToken)) {
                if (isFinal) {
                    VARIABLE_MAP.put(varName, new Variable(type, varName, currentToken.getValue(), true));
                } else {
                    VARIABLE_MAP.put(varName, new Variable(type, varName, currentToken.getValue(), false));
                }
            } else {
                throw new TypeException("The variable " + varName + " is not of type " + type, + currentToken.getLine(), currentToken.getStart());
            }
            advance();
            if (currentToken.getType() != TokenType.SEMICOLON) {
                throw new SemiColonException(currentToken.getLine(), currentToken.getStart());
            }
            advance();
            return true;
        } else if (isVariableAssignment()) {
          String varName = currentToken.getValue();
            advance(2);
            if (VARIABLE_MAP.containsKey(varName)) {
                if (VARIABLE_MAP.get(varName).isFinal()) {
                    throw new RuntimeException("Cannot assign a value to a final variable.\nLine: " + currentToken.getLine());
                }

                if (checkCorrespondentTypeVariable(VARIABLE_MAP.get(varName).getType(), currentToken)) {
                    VARIABLE_MAP.get(varName).setValue(currentToken.getValue());
                    advance();
                    if (currentToken.getType() != TokenType.SEMICOLON) {
                        throw new SemiColonException(currentToken.getLine(), currentToken.getStart());
                    }
                    advance();
                    return true;
                } else {
                    throw new RuntimeException("The variable " + varName + " is not of type " + VARIABLE_MAP.get(varName).getType() + ".\nLine: " + currentToken.getLine());
                }
            } else {
                throw new UnknownVariableException(varName, currentToken.getLine(), currentToken.getStart());
            }
        } else if (isFunctionDeclaration()) {
            advance();
            String funcName = currentToken.getValue();
            advance(3);
            ArrayList<Variable> args = getFuncDeclarationArguments();
            advance();
            ArrayList<Token> bodyToken = getFunctionBody();
            Parser bodyParser = new Parser(bodyToken);

            for (Variable arg : args) {
                bodyParser.VARIABLE_MAP.put(arg.getName(), arg);
            }

            if (FUNCTIONS.containsKey(funcName)) {
                throw new RuntimeException("The function " + funcName + " is already declared.\nLine: " + currentToken.getLine());
            }

            FUNCTIONS.put(funcName, new FunctionStatement(funcName, args, bodyParser));

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
        advance(2);
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

    private ArrayList<Variable> getFuncDeclarationArguments() {
        advance();
        // type: nom
        ArrayList<Variable> varsToReturn = new ArrayList<>();

        while (currentToken.getType() != TokenType.RIGHT_PARENTHESIS && tokens.indexOf(currentToken) + 1 < tokens.size()) {
            if (currentToken.getType() == TokenType.COMMA) {
                advance();
                continue;
            }

            if (isFuncParameterDeclaration()) {
                String type = currentToken.getValue();
                advance(2);
                varsToReturn.add(new Variable(type, currentToken.getValue(), null, false));
                advance();
            } else {
                throw new TypeException("Unknown type " + currentToken.getValue(), + currentToken.getLine(), currentToken.getStart());
            }
        }

        return varsToReturn;
    }

    private boolean isFuncParameterDeclaration() {
        return Arrays.asList(types).contains(currentToken.getValue()) && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.IDENTIFIER;
    }

    private boolean isVariableDeclaration() {
        if (Arrays.asList(types).contains(currentToken.getValue()) && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 3).getType() == TokenType.EQUALS) {
            return true;
        } else {
            if (currentToken.getType() == TokenType.FINAL && Arrays.asList(types).contains(tokens.get(tokens.indexOf(currentToken) + 1).getValue()) && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 3).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 4).getType() == TokenType.EQUALS) {
               return true;
            } else if (Arrays.asList(types).contains(tokens.get(tokens.indexOf(currentToken) + 3).getValue())) {
                throw new VariableNameException("Variable name can't be a type", currentToken.getLine(), currentToken.getStart());
            }

            if (Arrays.asList(types).contains(tokens.get(tokens.indexOf(currentToken) + 2).getValue())) {
                throw new VariableNameException("Variable name can't be a type", currentToken.getLine(), currentToken.getStart());
            }
            return false;
        }
    }

    private boolean isFunctionDeclaration() {
        return currentToken.getType() == TokenType.FUNC && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 3).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 4).getType() == TokenType.LEFT_PARENTHESIS;
    }

    private ArrayList<Token> getFunctionBody() {
        ArrayList<Token> tokensToReturn = new ArrayList<>();
        advance();
        int pos = 0;
        int line = 1;
        while (currentToken.getType() != TokenType.RIGHT_BRACE) {
            tokensToReturn.add(new Token(currentToken.getType(), currentToken.getValue(), pos, line));

            if (tokens.indexOf(currentToken) + 1 < tokens.size()) {
                if (tokens.get(tokens.indexOf(currentToken) + 1).getLine() - line < line) {
                    line++;
                }

                pos++;
                advance();
            } else {
                throw new RuntimeException("Unterminated function body");
            }
        }

        tokensToReturn.add(new Token(TokenType.EOF, "EOF", pos, line));
        advance();

        return tokensToReturn;
    }

    private boolean isFuncCall() {
        return currentToken.getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.LEFT_PARENTHESIS;
    }

    private boolean isFinalVariableDeclaration() {
        return currentToken.getType() == TokenType.FINAL;
    }

    private boolean isVariableAssignment() {
        return currentToken.getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.EQUALS;
    }

    private boolean checkCorrespondentTypeVariable(String type, Token value) {
        if (value.getType() == TokenType.IDENTIFIER) {
            if (VARIABLE_MAP.containsKey(value.getValue())) {
                return VARIABLE_MAP.get(value.getValue()).getClass().getSimpleName().toLowerCase().equals(type);
            } else {
                throw new UnknownVariableException(value.getValue(), value.getLine(), value.getStart());
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
    private void advance(int number) {
        currentToken = tokens.get(tokens.indexOf(currentToken) + number);
    }
}