package studio.karllang.karl.errors.runtime

import studio.karllang.karl.errors.Error

open class RuntimeError(filename: String, message: String, line: Int, pos: Int) : Error("Syntax Error", message, filename, line, pos)