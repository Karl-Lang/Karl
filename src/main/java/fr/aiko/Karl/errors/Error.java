package fr.aiko.Karl.errors;

import java.util.Arrays;

public class Error {
    private final String errorName;
    private final String message;
    private final String fileName;
    private final int line;

    public Error(String errorName, String message, String fileName, int line) {
        this.errorName = errorName;
        this.message = message;
        this.fileName = fileName;
        this.line = line;

        print();
    }

    public void print() {
        System.err.println("\u001B[31m" + errorName + ": " + message + "\u001B[0m");
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement main = stackTrace[stackTrace.length - 1];
        System.err.println("\u001B[31m" + "    at " + fileName + "(" + fileName + ":" + line + ")" + "\u001B[0m");
        System.err.println("\u001B[31m" + "    at " + main.getFileName() + "(" + main.getFileName() + ":" + main.getLineNumber() + ")" + "\u001B[0m");
        stackTrace = Arrays.copyOf(stackTrace, stackTrace.length - 1);
        for (StackTraceElement element : stackTrace) {
            if (!element.getClassName().startsWith("fr.aiko.Karl")) continue;
            System.err.println("\u001B[31m" + "    at " + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")" + "\u001B[0m");
        }

        System.exit(0);
    }
}
