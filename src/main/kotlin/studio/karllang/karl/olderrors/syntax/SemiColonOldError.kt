package studio.karllang.karl.olderrors.syntax

class SemiColonOldError(fileName: String, line: Int, pos: Int) : SyntaxOldError(fileName, "Missing semicolon", line, pos)