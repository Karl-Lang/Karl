package studio.karllang.karl.errors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import studio.karllang.karl.modules.Colors;

/**
 * Represents an error in a programming environment. Errors are used to report issues or problems
 * encountered during code execution.
 */
public class Error {
  private final String errorName; // The name or type of the error.
  private final String message; // A description of the error.
  private final String path; // The file path where the error occurred.
  private final int line; // The line number in the source code where the error occurred.
  private final int position; // The position within the line where the error occurred.

  /**
   * Constructs a new Error object with the specified error details and prints the error message.
   *
   * @param errorName The name or type of the error.
   * @param message A description of the error.
   * @param path The file path where the error occurred.
   * @param line The line number in the source code where the error occurred.
   * @param position The position within the line where the error occurred.
   */
  public Error(String errorName, String message, String path, int line, int position) {
    this.errorName = errorName;
    this.message = message;
    this.path = path;
    this.line = line;
    this.position = getPosition(position);

    print();
  }

  /**
   * Prints the error message, including the error type, description, file path, line, and
   * indicator.
   */
  public void print() {
    String fileName = path.substring(path.lastIndexOf("/") + 1);
    System.err.println(
        Colors.RED
            + "-- "
            + errorName.toUpperCase()
            + " ------------------------------------------------ "
            + fileName
            + Colors.RESET
            + "\n");
    System.err.println(
        Colors.WHITE + "Description: " + "\u001B[0m" + Colors.RED + message + Colors.RESET);
    System.err.println(
        Colors.WHITE + "File path: " + "\u001B[0m" + Colors.RED + path + Colors.RESET + "\n");
    System.err.println(Colors.RED + line + " | " + getLine() + Colors.RESET);
    System.err.println(Colors.RED + "   " + printIndicator() + Colors.RESET);
    System.err.println(
        Colors.RED
            + "--------------------------------------------------------------------------"
            + Colors.RESET
            + "\n");

    System.exit(0);
  }

  /**
   * Retrieves the content of the line where the error occurred.
   *
   * @return The content of the error line in the source code.
   */
  private String getLine() {
    try {
      return Files.readAllLines(Path.of(path)).get(line - 1);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Generates an error indicator to mark the position of the error in the line.
   *
   * @return A string representing the error indicator.
   */
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

  /**
   * Calculates the position within the line where the error occurred.
   *
   * @param givePos The initial position of the error within the file.
   * @return The adjusted position within the line.
   */
  private int getPosition(int givePos) {
    try {
      String[] lines =
          Files.readAllLines(Path.of(path)).subList(0, line - 1).toArray(new String[0]);
      int pos = Arrays.stream(lines).mapToInt(String::length).sum();
      return givePos - pos;
    } catch (IOException ignored) {
      return givePos;
    }
  }
}
