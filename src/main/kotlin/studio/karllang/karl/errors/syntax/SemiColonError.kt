package studio.karllang.karl.errors.syntax

class SemiColonError(fileName: String, line: Int, pos: Int) : SyntaxError(fileName, "Missing semicolon", line, pos)