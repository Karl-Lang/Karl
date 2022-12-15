package studio.karllang.karl.errors.runtime

import studio.karllang.karl.errors.Error

open class RuntimeError(message: String, filename: String, line: Int, pos: Int) : Error("Syntax Error", message, filename, line, pos)