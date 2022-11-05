package fr.aiko.Karl.parser.ast;

import fr.aiko.Karl.parser.Parser;
import fr.aiko.Karl.parser.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VariableAssignmentStatement extends Statement {
    private final String varName;
    private final String value;
    private final Map<String, Variable> VARIABLE_MAP;
    private final Token token;

    public VariableAssignmentStatement(String varName, String value, Map<String, Variable> varMap, Token token) {
        this.varName = varName;
        this.value = value;
        this.VARIABLE_MAP = varMap;
        this.token = token;
    }

    @Override
    public void execute() {
        this.VARIABLE_MAP.get(this.varName).setValue(this.value);
    }

    public Map<String, Variable> refreshVariables() {
        return this.VARIABLE_MAP;
    }

    public Token getToken() {
        return this.token;
    }
}
