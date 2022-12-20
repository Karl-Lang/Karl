package studio.karllang.karl.errors.runtime


class NumberError(
    pos: Int,
    errLine: Int,
    errLineString: String
) : RuntimeError("Number Error", pos, errLine, errLineString)