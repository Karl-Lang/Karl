package fr.aiko.Ryoko.parser.ErrorManager;

public class UnknownVariableException extends RyokoException {
    public UnknownVariableException(String varName, int line, int position) {
        super("Unknown variable : " + varName, line, position);
    }
}
