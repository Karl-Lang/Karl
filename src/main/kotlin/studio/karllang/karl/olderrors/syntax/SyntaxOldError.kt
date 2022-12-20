package studio.karllang.karl.olderrors.syntax

import studio.karllang.karl.olderrors.OldError

open class SyntaxOldError(filename: String, message: String, line: Int, pos: Int) : OldError("Syntax Error", message, filename, line, pos)