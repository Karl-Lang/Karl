package fr.aiko.ryoko.exceptions;

public class NotDeclaredVariableException extends RyukoException {
    public NotDeclaredVariableException(String variableName) {
        super(String.format("Variable '%s' is not declared.", variableName));
    }
}
