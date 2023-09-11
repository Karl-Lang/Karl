package studio.karllang.karl.errors.SyntaxError;

public class SemiColonError extends SyntaxError {
  public SemiColonError(String filePath, int line, int position) {
    super("Missing semi colon", filePath, line, position);
  }
}
