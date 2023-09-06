package studio.karllang.karl.errors.RuntimeError;

import studio.karllang.karl.errors.Error;

public class NumberError extends Error {
  public NumberError(String message, String fileName, int line, int position) {
    super("Number Error", message, fileName, line, position);
  }
}
