package studio.karllang.karl.errors.FileError;

import studio.karllang.karl.std.Colors;

public class FileError {
  private final String path;

  public FileError(String path) {
    this.path = path;

    print();
  }

  public void print() {
    String fileName = path.substring(path.lastIndexOf("/") + 1);
    System.err.println(
        Colors.RED
            + "-- "
            + "FILEERROR"
            + " ------------------------------------------------ "
            + fileName
            + Colors.RESET
            + "\n");
    System.err.println(
        Colors.WHITE
            + "Description: "
            + "\u001B[0m"
            + Colors.RED
            + "The file must be a .karl file"
            + Colors.RESET);
    System.err.println(
        Colors.WHITE + "File path: " + "\u001B[0m" + Colors.RED + path + Colors.RESET + "\n");
    System.err.println(
        Colors.RED
            + "--------------------------------------------------------------------------"
            + Colors.RESET
            + "\n");

    System.exit(0);
  }
}
