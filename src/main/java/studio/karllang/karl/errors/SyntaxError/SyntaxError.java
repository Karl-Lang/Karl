package studio.karllang.karl.errors.SyntaxError;

import studio.karllang.karl.errors.Error;

public class SyntaxError extends Error {
  public SyntaxError(String message, String fileName, int line, int position) {
    super("Syntax Error", message, fileName, line, position);
  }
}
