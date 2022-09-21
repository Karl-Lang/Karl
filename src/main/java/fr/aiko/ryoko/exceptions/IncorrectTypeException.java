package fr.aiko.ryoko.exceptions;

public class IncorrectTypeException extends RyukoException {
    public IncorrectTypeException(String name) {
        super(String.format("Incorrect type : '%s'", name));
    }
}
