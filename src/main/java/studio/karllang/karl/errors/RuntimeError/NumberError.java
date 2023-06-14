package studio.karllang.karl.errors.RuntimeError;

import studio.karllang.karl.errors.Error;

public class NumberError extends Error {
    public NumberError(String message, String filePath, int line, int position) {
        super("Number Error", message, filePath, line, position);
    }
}
