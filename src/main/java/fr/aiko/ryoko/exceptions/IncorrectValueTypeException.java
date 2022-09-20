package fr.aiko.ryoko.exceptions;

public class IncorrectValueTypeException extends RyukoException{
    public IncorrectValueTypeException(String varName, String value, String exceptedType) {
        super(String.format("Incorrect value type for variable '%s' : '%s' is not a '%s'", varName, value, exceptedType));
    }
}
