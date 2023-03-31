package me.aikoo.Karl.errors.RuntimeError;

public class DivisionByZeroError extends RuntimeError {
    public DivisionByZeroError(String fileName, int line, int pos) {
        super("Division by zero", fileName, line, pos);
    }
}
