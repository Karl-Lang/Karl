package studio.karllang.karl.errors.RuntimeError;

import studio.karllang.karl.errors.Error;

public class TypeError extends Error {
  public TypeError(String message, String fileName, int line, int position) {
    super("Type Error", message, fileName, line, position);
  }
}
