package fr.aiko.Ryoko.ErrorManager;

public class VariableNameException extends RyokoException {
    public VariableNameException(String message, int line, int position) {
        super(message, line, position);
    }
}
