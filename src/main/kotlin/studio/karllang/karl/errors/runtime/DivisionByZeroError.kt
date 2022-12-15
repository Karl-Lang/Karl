package studio.karllang.karl.errors.runtime

class DivisionByZeroError(fileName: String, line: Int, pos: Int) : RuntimeError( "Division by zero", fileName, line, pos)