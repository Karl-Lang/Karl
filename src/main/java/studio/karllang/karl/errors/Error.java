package studio.karllang.karl.errors;

import studio.karllang.karl.modules.Colors;

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
        this.position = getPosition(position);

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

        System.exit(0);
    }

    private String getLine() {
        try {
            return Files.readAllLines(Path.of(path)).get(line - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String printIndicator() {
        String line = getLine();
        if (line != null) {
            String[] array = line.split("");
            StringBuilder indicator = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                if (i == position) {
                    indicator.append("^");
                } else {
                    indicator.append(" ");
                }
            }

            if (!indicator.toString().endsWith("^") && !indicator.toString().contains("^")) {
                indicator.append("^");
            }

            return indicator.toString();
        } else {
            return "^";
        }
    }

    private int getPosition(int givePos) {
        try {
            String[] lines = Files.readAllLines(Path.of(path)).subList(0, line - 1).toArray(new String[0]);
            int pos = Arrays.stream(lines).mapToInt(String::length).sum();
            return givePos - pos;
        } catch (IOException ignored) {
            return givePos;
        }
    }
}
