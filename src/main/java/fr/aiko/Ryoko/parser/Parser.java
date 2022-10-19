package fr.aiko.Ryoko.parser;

import fr.aiko.Ryoko.ErrorManager.RuntimeError.RuntimeError;
import fr.aiko.Ryoko.ErrorManager.RuntimeError.TypeError;
import fr.aiko.Ryoko.ErrorManager.SyntaxError.SemiColonError;
import fr.aiko.Ryoko.ErrorManager.SyntaxError.SyntaxError;
import fr.aiko.Ryoko.parser.ast.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private final ArrayList<Token> tokens;
    private final String fileName;
    private final ArrayList<Statement> statements = new ArrayList<>();
    private final Map<TokenType, String> FUNC_CALL = new HashMap<>();
    public final Map<String, Variable> VARIABLE_MAP = new HashMap<>();
    private final Map<String, FunctionStatement> FUNCTIONS = new HashMap<>();
    private final String[] variableTypes = {"int", "float", "string", "bool", "char"};
    private Token currentToken;

    public Parser(ArrayList<Token> tokens, String fileName) {
        this.tokens = tokens;
        this.fileName = fileName;
        currentToken = tokens.get(0);

        FUNC_CALL.put(TokenType.SHOW, "SYSTEM_SHOW");
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
                    new RuntimeError("Unknown statement : " + currentToken.getValue(), fileName, currentToken.getLine());
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
            String funcName = currentToken.getValue();
            ArrayList<Token> args = getFuncCallArguments();

            if (args.size() == 0) {
                new RuntimeError("Excepted at least one argument for function " + funcName, fileName, currentToken.getLine());
            }

            if (tokens.get(tokens.indexOf(currentToken)).getType() != TokenType.SEMICOLON) {
                new SemiColonError(fileName, currentToken.getLine());
            }

            StringBuilder contentToPrint = new StringBuilder();

            for (Token arg : args) {
                if (arg.getType() == TokenType.IDENTIFIER) {
                    if (VARIABLE_MAP.containsKey(arg.getValue())) {
                        contentToPrint.append(VARIABLE_MAP.get(arg.getValue()).getValue());
                    } else {
                        new RuntimeError("Unknown variable: " + arg.getValue(), fileName, arg.getLine());
                    }
                } else {
                    contentToPrint.append(arg.getValue());
                }
            }

            return new PrintStatement(contentToPrint.toString());
        } else if (isFuncCall()) {
            String funcName = currentToken.getValue();
            if (!FUNCTIONS.containsKey(funcName)) {
                new RuntimeError("Unknown function : " + funcName, fileName, currentToken.getLine());
            }

            ArrayList<Token> args = getFuncCallArguments();
            FunctionStatement function = FUNCTIONS.get(funcName);

            if (args.size() != function.argsNumber) {
                new RuntimeError("Excepted " + function.argsNumber + " arguments for function " + funcName + " but got " + args.size() + " arguments.", fileName, currentToken.getLine());
            }

            function.parser.VARIABLE_MAP.forEach((key, value) -> {
                for (int i = 0; i < args.size(); i++) {
                    Token token = args.get(i);
                    String varType = function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).getType();

                    if (token.getType().toString().toLowerCase().equals(varType) || token.getType() == TokenType.IDENTIFIER) {
                        if (token.getType() == TokenType.IDENTIFIER) {
                            if (VARIABLE_MAP.containsKey(token.getValue())) {
                                if (VARIABLE_MAP.get(token.getValue()).getType().equals(varType)) {
                                    function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).setValue(VARIABLE_MAP.get(token.getValue()).getValue());
                                } else {
                                    new TypeError("Excepted type " + varType + " for argument : " + function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).getName() + " in function " + funcName + ". The entered value type is : " + VARIABLE_MAP.get(token.getValue()).getType(), fileName, token.getLine());
                                }
                            } else {
                                new RuntimeError("Unknown variable: " + token.getValue(), fileName, token.getLine());
                            }
                        } else {
                            function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).setValue(token.getValue());
                        }
                    } else {
                        new TypeError("Excepted type " + varType + " for argument " + function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).getName() + " in function " + funcName + ".\nThe entered value type is : " + token.getType().toString(), fileName, token.getLine());
                    }
                }
            });

            return function;
        } else if (currentToken.getType() == TokenType.IF) {
            advance();
            if (currentToken.getType() != TokenType.LEFT_PARENTHESIS) {
                new SyntaxError("Excepted '(' after 'if' keyword", fileName, currentToken.getLine());
            }

            advance();
            boolean condition = parseIfCondition();
            advance();
            ArrayList<Token> ifBody = getFunctionBody();
            Parser ifParser = new Parser(ifBody, fileName);
            return new IfStatement(condition, ifParser.parse());
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
                new TypeError("Excepted type " + type + " for variable \"" + varName + "\" but got " + currentToken.getType().toString().toLowerCase(), fileName, currentToken.getLine());
            }
            advance();
            if (currentToken.getType() != TokenType.SEMICOLON) {
                new SemiColonError(fileName, currentToken.getLine());
            }
            advance();
            return true;
        } else if (isVariableAssignment()) {
            String varName = currentToken.getValue();
            advance(2);
            if (VARIABLE_MAP.containsKey(varName)) {
                if (VARIABLE_MAP.get(varName).isFinal()) {
                    new RuntimeError("Cannot assign a value to a final variable.", fileName, currentToken.getLine());
                }

                if (checkCorrespondentTypeVariable(VARIABLE_MAP.get(varName).getType(), currentToken)) {
                    VARIABLE_MAP.get(varName).setValue(currentToken.getValue());
                    advance();
                    if (currentToken.getType() != TokenType.SEMICOLON) {
                        new SemiColonError(fileName, currentToken.getLine());
                    }
                    advance();
                    return true;
                } else {
                    new TypeError("The variable " + varName + " is not of type " + VARIABLE_MAP.get(varName).getType(), fileName, currentToken.getLine());
                    return false;
                }
            } else {
                new RuntimeError("Unknown variable: " + currentToken.getValue(), fileName, currentToken.getLine());
                return false;
            }
        } else if (isFunctionDeclaration()) {
            advance();
            String funcName = currentToken.getValue();
            advance(3);
            ArrayList<Variable> args = getFuncDeclarationArguments();
            advance();
            ArrayList<Token> bodyToken = getFunctionBody();
            Parser bodyParser = new Parser(bodyToken, fileName);

            for (Variable arg : args) {
                bodyParser.VARIABLE_MAP.put(arg.getName(), arg);
            }

            if (FUNCTIONS.containsKey(funcName)) {
                new RuntimeError("The function " + funcName + " is already defined", fileName, currentToken.getLine());
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

    public boolean parseIfCondition() {
        TokenType[] operators = {TokenType.EQUAL, TokenType.NOT_EQUAL, TokenType.GREATER, TokenType.LESS, TokenType.GREATER_EQUAL, TokenType.LESS_EQUAL};

        ArrayList<Token> conditionTokens = new ArrayList<>();
        while (currentToken.getType() != TokenType.RIGHT_PARENTHESIS) {
            conditionTokens.add(currentToken);
            advance();
        }

        if (conditionTokens.size() == 0) new SyntaxError("If statement can't be empty", fileName, currentToken.getLine());

        ArrayList<Boolean> results = new ArrayList<>();
        Token currentConditionToken = conditionTokens.get(0);
        while (conditionTokens.indexOf(currentConditionToken) <= conditionTokens.size()) {
            if (currentConditionToken.getType() == TokenType.EXCLAMATION && conditionTokens.get(conditionTokens.indexOf(currentConditionToken) + 1).getType() == TokenType.IDENTIFIER) {
                if (VARIABLE_MAP.containsKey(conditionTokens.get(conditionTokens.indexOf(currentConditionToken) + 1).getValue())) {
                    if (VARIABLE_MAP.get(conditionTokens.get(conditionTokens.indexOf(currentConditionToken) + 1).getValue()).getValue().equals("true")) {
                        results.add(false);
                    } else {
                        results.add(true);
                    }
                    if (conditionTokens.indexOf(currentConditionToken) + 2 < conditionTokens.size()) currentConditionToken = conditionTokens.get(conditionTokens.indexOf(currentConditionToken) + 2);
                } else {
                    new RuntimeError("Unknown variable: " + conditionTokens.get(conditionTokens.indexOf(currentConditionToken) + 1).getValue(), fileName, currentConditionToken.getLine());
                }
            } else if (conditionTokens.indexOf(currentConditionToken) + 1 <= conditionTokens.size() && currentConditionToken.getValue().equals("=")) {
                if (parseOperator(currentConditionToken) == currentConditionToken.getType()) {
                    new SyntaxError("Invalid operator : " + currentConditionToken.getValue(), fileName, currentConditionToken.getLine());
                }
                Token left = conditionTokens.get(conditionTokens.indexOf(currentConditionToken) - 1);
                String operator = parseOperator(currentConditionToken).getName();
                currentConditionToken = conditionTokens.get(conditionTokens.indexOf(currentConditionToken) + 1);
                Token right = conditionTokens.get(conditionTokens.indexOf(currentConditionToken) + 1);

                // First, check for each if it is a variable, then get it value
                if (left.getType() == TokenType.IDENTIFIER) {
                    if (VARIABLE_MAP.containsKey(left.getValue())) {
                        left = new Token(TokenType.valueOf(VARIABLE_MAP.get(left.getValue()).getType().toUpperCase()), VARIABLE_MAP.get(left.getValue()).getValue(), left.getLine(), left.getStart());
                    } else {
                        new RuntimeError("Unknown variable: " + left.getValue(), fileName, left.getLine());
                    }
                }

                if (right.getType() == TokenType.IDENTIFIER) {
                    if (VARIABLE_MAP.containsKey(right.getValue())) {
                        right = new Token(TokenType.valueOf(VARIABLE_MAP.get(right.getValue()).getType()), VARIABLE_MAP.get(right.getValue()).getValue(), right.getLine(), right.getStart());
                    } else {
                        new RuntimeError("Unknown variable: " + right.getValue(), fileName, right.getLine());
                    }
                }

                if (right.getType() != left.getType()) {
                    new RuntimeError("Cannot compare " + left.getType() + " with " + right.getType(), fileName, currentConditionToken.getLine());
                }

                String leftValue = left.getValue();
                String rightValue = right.getValue();

                if (right.getType() == TokenType.INT) {
                    switch (operator) {
                        case "==" -> results.add(Integer.parseInt(leftValue) == Integer.parseInt(rightValue));
                        case "!=" -> results.add(Integer.parseInt(leftValue) != Integer.parseInt(rightValue));
                        case ">" -> results.add(Integer.parseInt(leftValue) > Integer.parseInt(rightValue));
                        case "<" -> results.add(Integer.parseInt(leftValue) < Integer.parseInt(rightValue));
                        case ">=" -> results.add(Integer.parseInt(leftValue) >= Integer.parseInt(rightValue));
                        case "<=" -> results.add(Integer.parseInt(leftValue) <= Integer.parseInt(rightValue));
                    }
                } else if (right.getType() == TokenType.STRING) {
                    if (operator.equals("==")) {
                        results.add(leftValue.equals(rightValue));
                    } else if (operator.equals("!=")) {
                        results.add(leftValue.equals(rightValue));
                    } else {
                        new RuntimeError("Cannot compare strings with " + operator, fileName, currentConditionToken.getLine());
                    }
                } else if (right.getType() == TokenType.BOOL) {
                    if (operator.equals("==")) {
                        results.add(leftValue.equals(rightValue));
                    } else if (operator.equals("!=")) {
                        results.add(leftValue.equals(rightValue));
                    } else {
                        new RuntimeError("Cannot compare booleans with " + operator, fileName, currentConditionToken.getLine());
                    }
                }

                currentConditionToken = conditionTokens.get(conditionTokens.indexOf(currentConditionToken) + 1);
            }

            if (conditionTokens.indexOf(currentConditionToken) + 1 < conditionTokens.size()) {
                currentConditionToken = conditionTokens.get(conditionTokens.indexOf(currentConditionToken) + 1);
            } else break;
        }

        return !results.contains(false);
    }

    private TokenType parseOperator(Token operator) {
        String[] primaryOperator = {"<", ">", "="};
        if (tokens.indexOf(operator) + 1 < tokens.size()) {
            return switch(tokens.get(tokens.indexOf(operator) + 1).getType()) {
                case LESS -> TokenType.LESS_EQUAL;
                case GREATER -> TokenType.GREATER_EQUAL;
                case EQUALS -> TokenType.EQUAL;
                default -> operator.getType();
            };
        } else return operator.getType();
    }

    private ArrayList<Token> getFuncCallArguments() {
        advance(2);
        ArrayList<Token> tokensToReturn = new ArrayList<>();

        while (currentToken.getType() != TokenType.RIGHT_PARENTHESIS && tokens.indexOf(currentToken) + 1 < tokens.size()) {
            if (currentToken.getType() == TokenType.COMMA) {
                advance();
                continue;
            }

            if (tokens.get(tokens.indexOf(currentToken) + 1).getType() != TokenType.COMMA && tokens.get(tokens.indexOf(currentToken) + 1).getType() != TokenType.RIGHT_PARENTHESIS) {
                new RuntimeError("Unexpected token " + tokens.get(tokens.indexOf(currentToken) + 1).getValue() + " in function call", fileName, currentToken.getLine());
            }

            tokensToReturn.add(currentToken);
            advance();
        }
        advance();

        return tokensToReturn;
    }

    private ArrayList<Variable> getFuncDeclarationArguments() {
        advance();
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
                new TypeError("Unknown type " + currentToken.getValue(), fileName, currentToken.getLine());
            }
        }

        return varsToReturn;
    }

    private boolean isFuncParameterDeclaration() {
        return Arrays.asList(variableTypes).contains(currentToken.getValue()) && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.IDENTIFIER;
    }

    private boolean isVariableDeclaration() {
        if (Arrays.asList(variableTypes).contains(currentToken.getValue()) && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 3).getType() == TokenType.EQUALS) {
            return true;
        } else {
            if (currentToken.getType() == TokenType.FINAL && Arrays.asList(variableTypes).contains(tokens.get(tokens.indexOf(currentToken) + 1).getValue()) && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 3).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 4).getType() == TokenType.EQUALS) {
                return true;
            } else if (Arrays.asList(variableTypes).contains(tokens.get(tokens.indexOf(currentToken) + 3).getValue())) {
                new SyntaxError("Invalid variable name: " + tokens.get(tokens.indexOf(currentToken) + 3).getValue(), fileName, currentToken.getLine());
            }

            if (Arrays.asList(variableTypes).contains(tokens.get(tokens.indexOf(currentToken) + 2).getValue())) {
                new SyntaxError("Invalid variable name: " + tokens.get(tokens.indexOf(currentToken) + 3).getValue(), fileName, currentToken.getLine());
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
        while (currentToken.getType() != TokenType.RIGHT_BRACE) {
            tokensToReturn.add(new Token(currentToken.getType(), currentToken.getValue(), currentToken.getStart(), currentToken.getLine()));

            if (tokens.indexOf(currentToken) + 1 < tokens.size()) {
                advance();
            } else {
                new SyntaxError("Unterminated function body", fileName, currentToken.getLine());
            }
        }

        tokensToReturn.add(new Token(TokenType.EOF, "EOF", currentToken.getStart(), currentToken.getLine()));
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
                new RuntimeError("Unknown variable: " + value.getValue(), fileName, value.getLine());
                return false;
            }
        } else {
            return switch (type) {
                case "int" -> value.getType() == TokenType.INT;
                case "float" -> value.getType() == TokenType.FLOAT;
                case "string" -> value.getType() == TokenType.STRING;
                case "bool" -> value.getType() == TokenType.BOOL;
                case "char" -> value.getType() == TokenType.CHAR;
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