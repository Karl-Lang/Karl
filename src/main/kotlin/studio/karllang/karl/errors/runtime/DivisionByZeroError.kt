package studio.karllang.karl.errors.runtime

class DivisionByZeroError(fileName: String, line: Int, pos: Int) : RuntimeError(fileName, "Division by zero", line, pos)