package studio.karllang.karl.errors.runtime

class DivisionByZeroError(
    pos: Int,
    errLine: Int,
    errLineString: String
) : RuntimeError("Division by zero", pos, errLine, errLineString)