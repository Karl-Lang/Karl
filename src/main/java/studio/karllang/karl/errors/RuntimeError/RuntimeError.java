package studio.karllang.karl.errors.RuntimeError;

import studio.karllang.karl.errors.Error;

/** Represents a runtime error in Karl source code. */
public class RuntimeError extends Error {
  public RuntimeError(String message, String filePath, int line, int position) {
    super("Runtime Error", message, filePath, line, position);
  }
}
