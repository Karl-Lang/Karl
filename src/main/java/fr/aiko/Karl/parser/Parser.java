package fr.aiko.Karl.parser;

import fr.aiko.Karl.ErrorManager.RuntimeError.RuntimeError;
import fr.aiko.Karl.ErrorManager.RuntimeError.TypeError;
import fr.aiko.Karl.ErrorManager.SyntaxError.SemiColonError;
import fr.aiko.Karl.ErrorManager.SyntaxError.SyntaxError;
import fr.aiko.Karl.parser.ast.*;

import java.util.*;

public class Parser {
    public Map<String, Variable> VARIABLE_MAP = new HashMap<>();
    private final ArrayList<String> systemFunctions = new ArrayList<>();
    private final ArrayList<Token> tokens;
    public final String fileName;
    private final ArrayList<Statement> statements = new ArrayList<>();
    private final Map<String, FunctionStatement> FUNCTIONS = new HashMap<>();
    private final String[] variableTypes = {"int", "float", "string", "bool", "char"};
    private final TokenType[] primaryOperatorsToken = {TokenType.EQUALS, TokenType.LESS, TokenType.GREATER};
    private Token currentToken;

    public Parser(ArrayList<Token> tokens, String fileName) {
        this.tokens = tokens;
        this.fileName = fileName;
        currentToken = tokens.get(0);

        systemFunctions.add("show");
    }

    public ArrayList<Statement> parse() {
        while (currentToken.getType() != TokenType.EOF) {
            if (isFuncCall()) parseFuncCall();
            else if (isIfStatement()) parseIfStatement();
            else if (isVariableDeclaration()) parseVariableDeclaration();
            else if (isVariableAssignment()) parseVariableAssignment();
            else if (isFunctionDeclaration()) parseFunctionDeclaration();
            else if (isIncrementDecrement()) parseIncrementDecrement();
            else if (isCommentary()) parseCommentary();
            else new RuntimeError("Unknown statement : " + currentToken.getValue(), fileName, currentToken.getLine());

            if (tokens.indexOf(currentToken) + 1 < tokens.size()) {
                advance();
            }
        }
        return statements;
    }

    private boolean isIncrementDecrement() {
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            if (tokens.indexOf(currentToken) + 1 < tokens.size()) {
                Token nextToken = tokens.get(tokens.indexOf(currentToken) + 1);
                if (nextToken.getType() == TokenType.PLUS || nextToken.getType() == TokenType.MINUS) {
                    if (tokens.indexOf(nextToken) + 1 < tokens.size()) {
                        Token nextNextToken = tokens.get(tokens.indexOf(nextToken) + 1);
                        return nextNextToken.getType() == nextToken.getType();
                    }
                }
            }
        }

        return false;
    }

    private void parseIncrementDecrement() {
        Variable variable = VARIABLE_MAP.get(currentToken.getValue());
        if (variable == null) new RuntimeError("Variable " + currentToken.getValue() + " is not defined", fileName, currentToken.getLine());
        assert variable != null;
        if (variable.isFinal()) new RuntimeError("Cannot increment/decrement a final variable: " + currentToken.getValue(), fileName, currentToken.getLine());
        advance();
        Token operator = tokens.get(tokens.indexOf(currentToken));
        advance(2);
        checkSemiColon();
        if (operator.getType() == TokenType.PLUS) {
            if (TokenType.valueOf(variable.getType().toUpperCase()) == TokenType.INT) {
                String value = Integer.toString(Integer.parseInt(variable.getValue()) + 1);
                statements.add(new VariableAssignmentStatement(variable.getName(), value, VARIABLE_MAP, currentToken));
            } else if (TokenType.valueOf(variable.getType().toUpperCase()) == TokenType.FLOAT) {
                String value = Float.toString(Float.parseFloat(variable.getValue()) + 1);
                statements.add(new VariableAssignmentStatement(variable.getName(), value, VARIABLE_MAP, currentToken));
            } else new TypeError("Cannot increment a " + variable.getType() + " variable", fileName, currentToken.getLine());
        } else if (operator.getType() == TokenType.MINUS) {
            if (TokenType.valueOf(variable.getType().toUpperCase()) == TokenType.INT) {
                String value = Integer.toString(Integer.parseInt(variable.getValue()) - 1);
                statements.add(new VariableAssignmentStatement(variable.getName(), value, VARIABLE_MAP, currentToken));
            } else if (TokenType.valueOf(variable.getType().toUpperCase()) == TokenType.FLOAT) {
                String value = Float.toString(Float.parseFloat(variable.getValue()) - 1);
                statements.add(new VariableAssignmentStatement(variable.getName(), value, VARIABLE_MAP, currentToken));
            } else new TypeError("Cannot decrement a " + variable.getType() + " variable", fileName, currentToken.getLine());
        }
    }

    private boolean isCommentary() {
        return currentToken.getType() == TokenType.DIVIDE && tokens.indexOf(currentToken) + 1 < tokens.size() && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.DIVIDE;
    }

    private void parseCommentary() {
        int baseLine = currentToken.getLine();
        while (currentToken.getLine() == baseLine && tokens.indexOf(currentToken) + 1 < tokens.size()) {
            advance();
        }

        advance(-1);
    }

    private boolean isIfStatement() {
        return currentToken.getType() == TokenType.IF && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.LEFT_PARENTHESIS;
    }

    private void parseIfStatement() {
        advance(2);
        ArrayList<Token> conditionTokens = new ArrayList<>();
        while (currentToken.getType() != TokenType.RIGHT_PARENTHESIS) {
            if (currentToken.getType() == TokenType.EOF)
                new SyntaxError("Missing ')' in if statement", fileName, currentToken.getLine());
            else if (currentToken.getType() == TokenType.RIGHT_PARENTHESIS) break;
            conditionTokens.add(currentToken);
            advance();
        }
        if (conditionTokens.size() == 0)
            new SyntaxError("If statement can't be empty", fileName, currentToken.getLine());

        boolean condition = parseCondition(conditionTokens);
        advance();
        if (currentToken.getType() != TokenType.MINUS || tokens.get(tokens.indexOf(currentToken) + 1).getType() != TokenType.GREATER)
            new SyntaxError("Missing '->' after if statement condition", fileName, currentToken.getLine());
        advance(2);
        ArrayList<Token> ifTokens = getFunctionBody();
        Parser ifParser = new Parser(ifTokens, fileName);
        for (Variable variable : VARIABLE_MAP.values()) {
            ifParser.VARIABLE_MAP.put(variable.getName(), variable);
        }
        if (isElseStatement()) {
            advance(); // else
            if (tokens.indexOf(currentToken) + 4 < tokens.size()) {
                advance();
                if (!isIfStatement()) {
                    if (currentToken.getType() != TokenType.MINUS || tokens.get(tokens.indexOf(currentToken) + 1).getType() != TokenType.GREATER)
                        new SyntaxError("Missing '->' after if statement condition", fileName, currentToken.getLine());
                    advance();
                    Token nextToken = tokens.get(tokens.indexOf(currentToken) + 1);
                    if (nextToken.getType() == TokenType.LEFT_BRACE) {
                        advance();
                        ArrayList<Token> elseTokens = getFunctionBody();
                        Parser elseParser = new Parser(elseTokens, fileName);
                        for (Variable variable : VARIABLE_MAP.values()) {
                            elseParser.VARIABLE_MAP.put(variable.getName(), variable);
                        }
                        ArrayList<Statement> elseStatements = elseParser.parse();
                        statements.add(new IfStatement(condition, ifParser.parse(), elseStatements));
                    } else {
                        advance();
                        if (isVariableAssignment()) parseVariableAssignment();
                        else if (isIncrementDecrement()) parseIncrementDecrement();
                        else if (isCommentary()) new RuntimeError("Unterminated else statement", fileName, currentToken.getLine());
                        else new RuntimeError("Unauthorized else statement", fileName, currentToken.getLine());
                    }
                } else if (isIfStatement()) {
                    parseIfStatement();
                }
            } else new SyntaxError("Unterminated else statement", fileName, currentToken.getLine());
        } else {
            statements.add(new IfStatement(condition, ifParser.parse(), null));
        }
    }

    private boolean isElseStatement() {
        return tokens.indexOf(currentToken) + 1 < tokens.size() && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.ELSE;
    }

    private boolean parseCondition(ArrayList<Token> conditionsTokens) {
        ArrayList<Boolean> results = new ArrayList<>();
        Token currentTok = conditionsTokens.get(0);
        while (conditionsTokens.indexOf(currentTok) < conditionsTokens.size() - 1) {
            if (currentTok.getType() == TokenType.RIGHT_PARENTHESIS) break;
            if (isFalseOperator(currentTok, conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 1))) {
                Token nextTok = conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 1);
                if (nextTok.getType() == TokenType.EXCLAMATION) {
                    // Make a new ArrayList without previous tokens
                    ArrayList<Token> newConditionsTokens = new ArrayList<>();
                    for (int i = conditionsTokens.indexOf(currentTok) + 1; i < conditionsTokens.size(); i++) {
                        newConditionsTokens.add(conditionsTokens.get(i));
                    }
                    results.add(!parseCondition(newConditionsTokens));
                    break;
                } else results.add(parseFalseOperator(conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 1)));
                if (conditionsTokens.indexOf(currentTok) + 2 < conditionsTokens.size()) {
                    currentTok = conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 2);
                } else break;
            } else if (isComparisonOperator(conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 1))) {
                int size = 2;
                TokenType operator = parseOperator(conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 1));
                if (Arrays.asList(primaryOperatorsToken).contains(operator)) {
                    size = 1;
                }
                results.add(parseComparisonOperator(currentTok, operator, conditionsTokens.get(conditionsTokens.indexOf(currentTok) + size + 1)));
                if (conditionsTokens.indexOf(currentTok) + 1 + size < conditionsTokens.size()) {
                    currentTok = conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 1 + size);
                } else break;
            }  else if (parseOperator(conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 1)) == TokenType.AND) {
                boolean condition1 = !results.contains(false);
                boolean condition2 = parseCondition(new ArrayList<>(conditionsTokens.subList(conditionsTokens.indexOf(currentTok) + 3, conditionsTokens.size())));
                results.clear();
                results.add(condition1 && condition2);
                break;
            } else if (parseOperator(conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 1)) == TokenType.OR) {
                boolean condition1 = !results.contains(false);
                boolean condition2 = parseCondition(new ArrayList<>(conditionsTokens.subList(conditionsTokens.indexOf(currentTok) + 3, conditionsTokens.size())));
                results.clear();
                results.add(condition1 || condition2);
                break;
            } else {
                new SyntaxError("Unknown operator : " + conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 1).getValue(), fileName, conditionsTokens.get(conditionsTokens.indexOf(currentTok) + 1).getLine());
            }
        }

        if (results.size() == 0) {
            if (currentTok.getValue().equals("true")) return true;
            else if (currentTok.getValue().equals("false")) return false;
            else if (currentTok.getType() == TokenType.IDENTIFIER) {
                if (VARIABLE_MAP.containsKey(currentTok.getValue())) {
                    Variable variable = VARIABLE_MAP.get(currentTok.getValue());
                    if (TokenType.valueOf(variable.getType().toUpperCase()) == TokenType.BOOL) {
                        if (variable.getValue().equals("true")) results.add(true);
                        else results.add(false);
                    } else new SyntaxError(currentTok.getValue() + " is not a boolean", fileName, currentTok.getLine());
                } else new SyntaxError("Unknown variable : " + currentTok.getValue(), fileName, currentTok.getLine());
            } else new SyntaxError("Unknown condition : " + currentTok.getValue(), fileName, currentTok.getLine());
        }

        return !results.contains(false);
    }

    private TokenType parseOperator(Token operator) {
        if (tokens.indexOf(operator) + 1 < tokens.size()) {
            if (operator.getType() == TokenType.EQUALS && tokens.get(tokens.indexOf(operator) + 1).getType() == TokenType.EQUALS) {
                return TokenType.EQUAL;
            } else if (operator.getType() == TokenType.LESS) {
                return tokens.get(tokens.indexOf(operator) + 1).getType() == TokenType.EQUALS ? TokenType.LESS_EQUAL : operator.getType();
            } else if (operator.getType() == TokenType.GREATER) {
                return tokens.get(tokens.indexOf(operator) + 1).getType() == TokenType.EQUALS ? TokenType.GREATER_EQUAL : operator.getType();
            } else if (operator.getType() == TokenType.EXCLAMATION) {
                return tokens.get(tokens.indexOf(operator) + 1).getType() == TokenType.EQUALS ? TokenType.NOT_EQUAL : operator.getType();
            } else if (operator.getType() == TokenType.BAR) {
                return tokens.get(tokens.indexOf(operator) + 1).getType() == TokenType.BAR ? TokenType.OR : operator.getType();
            } else if (operator.getType() == TokenType.AMP) {
                return tokens.get(tokens.indexOf(operator) + 1).getType() == TokenType.AMP ? TokenType.AND : operator.getType();
            } else return operator.getType();
        } else return operator.getType();
    }

    private boolean isFalseOperator(Token token, Token token1) {
        return token.getType() == TokenType.EXCLAMATION && (token1.getType() == TokenType.IDENTIFIER || token1.getType() == TokenType.BOOL || token1.getType() == TokenType.EXCLAMATION);
    }

    private boolean parseFalseOperator(Token token) {
        String value = token.getValue();
        if (token.getType() == TokenType.IDENTIFIER) {
            Variable var = VARIABLE_MAP.get(token.getValue());
            if (var == null) new RuntimeError("Variable " + token.getValue() + " doesn't exist", fileName, token.getLine());
            assert var != null;
            value = var.getValue();
        } return switch (value) {
            case "false", "null" -> true;
            default -> false;
        };
    }

    private boolean isComparisonOperator(Token token) {
        return token.getType() == TokenType.EQUALS || token.getType() == TokenType.NOT_EQUAL || token.getType() == TokenType.GREATER || token.getType() == TokenType.LESS || token.getType() == TokenType.GREATER_EQUAL || token.getType() == TokenType.LESS_EQUAL || (token.getType() == TokenType.EXCLAMATION && tokens.get(tokens.indexOf(token) + 1).getType() == TokenType.EQUALS);
    }

    private boolean parseComparisonOperator(Token left, TokenType operator, Token right) {
        if (left.getType() == TokenType.IDENTIFIER) {
            left = getValue(left);
        } else if (right.getType() == TokenType.IDENTIFIER) {
            right = getValue(right);
        }

        if (left.getType() != right.getType()) {
            new SyntaxError("Can't compare type " + left.getType().toString().toLowerCase() + " with type " + right.getType().toString().toLowerCase(), fileName, left.getLine());
        }

        if (left.getType() == TokenType.INT) {
            return switch (operator) {
                case EQUAL -> Integer.parseInt(left.getValue()) == Integer.parseInt(right.getValue());
                case NOT_EQUAL -> !left.getValue().equals(right.getValue());
                case GREATER -> Integer.parseInt(left.getValue()) > Integer.parseInt(right.getValue());
                case LESS -> Integer.parseInt(left.getValue()) < Integer.parseInt(right.getValue());
                case GREATER_EQUAL -> Integer.parseInt(left.getValue()) >= Integer.parseInt(right.getValue());
                case LESS_EQUAL -> Integer.parseInt(left.getValue()) <= Integer.parseInt(right.getValue());
                default -> {
                    new SyntaxError("Unknown operator: " + operator.getName(), fileName, left.getLine());
                    yield false;
                }
            };
        } else {
            return switch (operator) {
                case EQUAL -> left.getValue().equals(right.getValue());
                case NOT_EQUAL -> !left.getValue().equals(right.getValue());
                default -> {
                    new SyntaxError("Unknown operator " + operator.getName() + " for comparison with type " + left.getType().toString().toLowerCase(), fileName, left.getLine());
                    yield false;
                }
            };
        }
    }

    private Token getValue(Token left) {
        Variable var = VARIABLE_MAP.get(left.getValue());
        if (var == null) new RuntimeError("Variable " + left.getValue() + " doesn't exist", fileName, left.getLine());
        assert var != null;
        left = new Token(TokenType.valueOf(var.getType().toUpperCase()), var.getValue(), left.getLine(), left.getPosition());
        return left;
    }

    private boolean isFunctionDeclaration() {
        return currentToken.getType() == TokenType.FUNC && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 3).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 4).getType() == TokenType.LEFT_PARENTHESIS;
    }

    private void parseFunctionDeclaration() {
        advance();
        String name = currentToken.getValue();
        if (FUNCTIONS.containsKey(name)) {
            new RuntimeError("The function " + name + " is already defined", fileName, currentToken.getLine());
        }
        advance(3);
        ArrayList<Variable> parameters = getFunctionDeclarationArguments();
        advance();
        ArrayList<Token> bodyToken = getFunctionBody();
        Parser parser = new Parser(bodyToken, fileName);
        for (Variable arg : parameters) {
            parser.VARIABLE_MAP.put(arg.getName(), arg);
        }

        for (FunctionStatement function : FUNCTIONS.values()) {
            parser.FUNCTIONS.put(function.name, function);
        }

        // Check if bodyToken include a function declaration
        for (Token token : bodyToken) {
            if (token.getType() == TokenType.FUNC && tokens.get(tokens.indexOf(token) + 1).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(token) + 2).getType() == TokenType.COLON && tokens.get(tokens.indexOf(token) + 3).getType() == TokenType.COLON && tokens.get(tokens.indexOf(token) + 4).getType() == TokenType.LEFT_PARENTHESIS) {
                new RuntimeError("Cannot declare a function inside another function", fileName, token.getLine());
            }
        }

        FUNCTIONS.put(name, new FunctionStatement(name, parameters, parser));
    }

    private boolean isVariableDeclaration() {
        if (currentToken.getType() == TokenType.FINAL) {
            return Arrays.asList(variableTypes).contains(tokens.get(tokens.indexOf(currentToken) + 1).getValue()) && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 3).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 4).getType() == TokenType.EQUALS;
        } else
            return Arrays.asList(variableTypes).contains(currentToken.getValue()) && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.IDENTIFIER && tokens.get(tokens.indexOf(currentToken) + 3).getType() == TokenType.EQUALS;
    }

    private void parseVariableDeclaration() {
        boolean isFinal = currentToken.getType() == TokenType.FINAL;
        if (isFinal) advance();
        String type = currentToken.getValue();
        advance(2);
        String name = currentToken.getValue();
        checkValidVariableName(name);
        advance(2);
        Token value = currentToken;
        if (checkCorrespondentTypeVariable(type, value)) {
            String valueType = value.getType().toString().toLowerCase();
            if (valueType.equals("identifier")) {
                valueType = VARIABLE_MAP.get(value.getValue()).getType().toLowerCase();
            }
            new TypeError("Excepted type " + type + " for variable " + name + " but got type " + valueType, fileName, currentToken.getLine());
        }
        advance();

        if (value.getType() == TokenType.IDENTIFIER) {
            Variable var = VARIABLE_MAP.get(value.getValue());
            if (var == null) new RuntimeError("Variable " + value.getValue() + " doesn't exist", fileName, value.getLine());
            assert var != null;
            value = new Token(TokenType.valueOf(var.getType().toUpperCase()), var.getValue(), value.getLine(), value.getPosition());
        }
        VARIABLE_MAP.put(name, new Variable(type, name, value.getValue(), isFinal));
        checkSemiColon();
    }

    private boolean isVariableAssignment() {
        return currentToken.getType() == TokenType.IDENTIFIER && tokens.indexOf(currentToken) + 1 < tokens.size() && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.EQUALS;
    }

    private void parseVariableAssignment() {
        String name = currentToken.getValue();
        if (!VARIABLE_MAP.containsKey(name))
            new RuntimeError("Variable " + name + " is not declared", fileName, currentToken.getLine());
        Variable variable = VARIABLE_MAP.get(name);
        if (variable.isFinal())
            new RuntimeError("Cannot assign new value to a final variable", fileName, currentToken.getLine());
        advance(2);
        Token value = currentToken;
        String newValue = currentToken.getValue();
        if (checkCorrespondentTypeVariable(variable.getType(), value))
            new TypeError("Excepted type " + variable.getType() + " for variable " + name + " but got type " + value.getType().toString().toLowerCase(), fileName, currentToken.getLine());
        advance();
        checkSemiColon();
        statements.add(new VariableAssignmentStatement(name, newValue, this.VARIABLE_MAP, currentToken));
    }

    private boolean isFuncCall() {
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            return tokens.indexOf(currentToken) + 1 < tokens.size() && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.LEFT_PARENTHESIS;
        } else return false;
    }

    private void parseFuncCall() {
        if (isSystemFunction(currentToken.getValue()) && currentToken.getValue().equals("show")) {
            ArrayList<Token> args = getFuncCallArguments();
            if (args.size() == 0) {
                new RuntimeError("Excepted at least one argument for function " + "show", fileName, currentToken.getLine());
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

            statements.add(new PrintStatement(contentToPrint.toString()));
        } else {
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
                        new TypeError("Excepted type " + varType + " for argument " + function.parser.VARIABLE_MAP.get(function.args.get(i).getName()).getName() + " in function " + funcName + " but got " + token.getType().toString().toLowerCase() + " type", fileName, token.getLine());
                    }
                }
            });

            statements.add(function);
        }
    }

    private ArrayList<Token> getFuncCallArguments() {
        advance(2); // go to parameters
        ArrayList<Token> tokensToReturn = new ArrayList<>();

        while (currentToken.getType() != TokenType.RIGHT_PARENTHESIS && tokens.indexOf(currentToken) + 1 < tokens.size()) {
            if (currentToken.getType() == TokenType.COMMA) {
                advance();
                continue;
            } else if (tokens.get(tokens.indexOf(currentToken) + 1).getType() != TokenType.COMMA && tokens.get(tokens.indexOf(currentToken) + 1).getType() != TokenType.RIGHT_PARENTHESIS) {
                new RuntimeError("Unexpected token " + tokens.get(tokens.indexOf(currentToken) + 1).getValue() + " in function call", fileName, currentToken.getLine());
            }

            tokensToReturn.add(currentToken);
            advance();
        }

        advance();

        return tokensToReturn;
    }

    private ArrayList<Token> getFunctionBody() {
        ArrayList<Token> tokensToReturn = new ArrayList<>();
        advance();
        int braces = 1;
        while (braces != 1 || currentToken.getType() != TokenType.RIGHT_BRACE) { // currentToken.getType() != TokenType.RIGHT_BRACE && braces != 0
            tokensToReturn.add(currentToken);
            if (currentToken.getType() == TokenType.LEFT_BRACE) {
                braces++;
            } else if (currentToken.getType() == TokenType.RIGHT_BRACE) {
                braces--;
            }

            if (tokens.indexOf(currentToken) + 1 < tokens.size()) {
                advance();
            } else {
                new SyntaxError("Unterminated function body", fileName, currentToken.getLine());
            }
        }

        tokensToReturn.add(new Token(TokenType.EOF, "EOF", currentToken.getPosition(), currentToken.getLine()));

        return tokensToReturn;
    }

    private boolean isFuncParameterDeclaration() {
        return Arrays.asList(variableTypes).contains(currentToken.getValue()) && tokens.get(tokens.indexOf(currentToken) + 1).getType() == TokenType.COLON && tokens.get(tokens.indexOf(currentToken) + 2).getType() == TokenType.IDENTIFIER;
    }

    private ArrayList<Variable> getFunctionDeclarationArguments() {
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
                checkValidVariableName(currentToken.getValue());
                varsToReturn.add(new Variable(type, currentToken.getValue(), null, false));
                advance();
            } else {
                new TypeError("Unknown type " + currentToken.getValue(), fileName, currentToken.getLine());
            }
        }

        return varsToReturn;
    }

    private boolean isSystemFunction(String name) {
        return systemFunctions.contains(name);
    }

    private boolean checkCorrespondentTypeVariable(String type, Token value) {
        if (value.getType() == TokenType.IDENTIFIER) {
            if (VARIABLE_MAP.containsKey(value.getValue())) {
                return !VARIABLE_MAP.get(value.getValue()).getType().equalsIgnoreCase(type);
            } else {
                new RuntimeError("Unknown variable: " + value.getValue(), fileName, value.getLine());
                return true;
            }
        } else {
            return !switch (type) {
                case "int" -> value.getType() == TokenType.INT;
                case "float" -> value.getType() == TokenType.FLOAT;
                case "string" -> value.getType() == TokenType.STRING;
                case "bool" -> value.getType() == TokenType.BOOL;
                case "char" -> value.getType() == TokenType.CHAR;
                default -> false;
            };
        }
    }

    private void checkValidVariableName(String name) {
        if (Arrays.asList(variableTypes).contains(name) || systemFunctions.contains(name) || FUNCTIONS.containsKey(name)) {
            new SyntaxError("Invalid variable name: " + name, fileName, currentToken.getLine());
        }

        if (VARIABLE_MAP.containsKey(name)) {
            new SyntaxError("Variable " + name + " already exists", fileName, currentToken.getLine());
        }
    }

    private void checkSemiColon() {
        if (tokens.get(tokens.indexOf(currentToken)).getType() != TokenType.SEMICOLON) {
            new SemiColonError(fileName, currentToken.getLine());
        }
    }

    private void advance() {
        currentToken = tokens.get(tokens.indexOf(currentToken) + 1);
    }

    private void advance(int number) {
        currentToken = tokens.get(tokens.indexOf(currentToken) + number);
    }

    public Map<String, Variable> getVariables() {
        return VARIABLE_MAP;
    }

    public void setVariables(Map<String, Variable> variables) {
        this.VARIABLE_MAP = variables;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public String getFileName() {
        return fileName;
    }
}
