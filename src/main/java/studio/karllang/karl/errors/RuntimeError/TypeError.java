package studio.karllang.karl.errors.RuntimeError;

import studio.karllang.karl.errors.Error;

/** Represents a type-related error in Karl source code. */
public class TypeError extends Error {
  public TypeError(String message, String filePath, int line, int position) {
    super("Type Error", message, filePath, line, position);
  }
}
