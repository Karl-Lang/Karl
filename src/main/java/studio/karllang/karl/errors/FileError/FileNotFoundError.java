package studio.karllang.karl.errors.FileError;

import studio.karllang.karl.Constants;

public class FileNotFoundError {
    private final String path;

    public FileNotFoundError(String path) {
        this.path = path;

        print();
    }

    public void print() {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        System.err.println(Constants.RED + "-- " + "FILENOTFOUND" + " ------------------------------------------------ " + fileName + Constants.RESET + "\n");
        System.err.println(Constants.WHITE + "Description: " + "\u001B[0m" + Constants.RED + "The file " + fileName + " doesn't exist" + Constants.RESET);
        System.err.println(Constants.WHITE + "File path: " + "\u001B[0m" + Constants.RED + path + Constants.RESET + "\n");
        System.err.println(Constants.RED + "--------------------------------------------------------------------------" + Constants.RESET + "\n");

        System.exit(0);
    }
}
