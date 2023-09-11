package studio.karllang.karl.errors.FileError;

import studio.karllang.karl.modules.Colors;

public class FileNotFoundError {
  private final String path;

  public FileNotFoundError(String path) {
    this.path = path;

    print();
  }

  public void print() {
    String fileName = path.substring(path.lastIndexOf("/") + 1);
    System.err.println(
        Colors.RED
            + "-- "
            + "FILENOTFOUND"
            + " ------------------------------------------------ "
            + fileName
            + Colors.RESET
            + "\n");
    System.err.println(
        Colors.WHITE
            + "Description: "
            + "\u001B[0m"
            + Colors.RED
            + "The file "
            + fileName
            + " doesn't exist"
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
