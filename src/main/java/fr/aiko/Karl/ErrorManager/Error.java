package fr.aiko.Karl.ErrorManager;

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
        System.err.println(errorName + ": " + message);
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement main = stackTrace[stackTrace.length - 1];
        System.err.println("    at " + fileName + "(" + fileName + ":" + line + ")");
        System.err.println("    at " + main.getFileName() + "(" + main.getFileName() + ":" + main.getLineNumber() + ")");
        stackTrace = Arrays.copyOf(stackTrace, stackTrace.length - 1);
        for (StackTraceElement element : stackTrace) {
            System.err.println("    at " + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
        }

        System.exit(0);
    }
}
