package studio.karllang.karl.errors.syntax

import studio.karllang.karl.errors.Error

open class SyntaxError(filename: String, message: String, line: Int, pos: Int) : Error("Syntax Error", message, filename, line, pos)