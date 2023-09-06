package studio.karllang.karl.errors.RuntimeError;

import studio.karllang.karl.errors.Error;

public class RuntimeError extends Error {
  public RuntimeError(String message, String fileName, int line, int position) {
    super("Runtime Error", message, fileName, line, position);
  }
}
