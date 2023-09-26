package studio.karllang.karl.errors.SyntaxError;

import studio.karllang.karl.errors.Error;

/** Represents a syntax error in Karl source code. */
public class SyntaxError extends Error {
  public SyntaxError(String message, String filePath, int line, int position) {
    super("Syntax Error", message, filePath, line, position);
  }
}
