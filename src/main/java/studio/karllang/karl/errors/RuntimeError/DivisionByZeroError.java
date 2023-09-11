package studio.karllang.karl.errors.RuntimeError;

public class DivisionByZeroError extends RuntimeError {
  public DivisionByZeroError(String filePath, int line, int pos) {
    super("Division by zero", filePath, line, pos);
  }
}
