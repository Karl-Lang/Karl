package fr.aiko.Karl.errors;

import fr.aiko.Karl.std.Colors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Error {
    private final String errorName;
    private final String message;
    private final String path;
    private final int line;
    private final int position;

    public Error(String errorName, String message, String path, int line, int position) {
        this.errorName = errorName;
        this.message = message;
        this.path = path;
        this.line = line;
        this.position = position;

        print();
    }

    public void print() {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        System.err.println(Colors.RED + "-- " + errorName.toUpperCase() + " ------------------------------------------------ " + fileName + Colors.RESET + "\n");
        System.err.println(Colors.WHITE + "Description: " + "\u001B[0m" + Colors.RED + message + Colors.RESET);
        System.err.println(Colors.WHITE + "File path: " + "\u001B[0m" + Colors.RED + path + Colors.RESET + "\n");
        System.err.println(Colors.RED + line + " | " + getLine() + Colors.RESET);
        System.err.println(Colors.RED + "   " + printIndicator() + Colors.RESET);
        System.err.println(Colors.RED + "--------------------------------------------------------------------------" + Colors.RESET + "\n");

        Arrays.asList(Thread.currentThread().getStackTrace()).forEach(System.out::println);

        System.exit(0);
    }

    private String getLine() {
        try {
            return Files.readAllLines(Path.of(path)).get(line - 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFile() {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String printIndicator() {
        String line = getLine();
        int pos = position - getFile().indexOf(line);
        char[] chars = new char[pos - 1];
        Arrays.fill(chars, ' ');
        return new String(chars) + "^";
    }
}
