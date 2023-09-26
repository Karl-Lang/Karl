package studio.karllang.karl.errors.SyntaxError;

/**
 * Represents a specific type of syntax error related to a missing semicolon in the source code. It
 * extends the 'SyntaxError' class.
 */
public class SemiColonError extends SyntaxError {
  /**
   * Constructs a new 'SemiColonError' object indicating a missing semicolon error.
   *
   * @param filePath The file path where the missing semicolon error occurred.
   * @param line The line number in the source code where the error occurred.
   * @param position The position within the line where the error occurred.
   */
  public SemiColonError(String filePath, int line, int position) {
    super("Missing semicolon", filePath, line, position);
  }
}
