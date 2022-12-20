package studio.karllang.karl.olderrors.runtime

import studio.karllang.karl.olderrors.OldError

open class RuntimeOldError(message: String, filename: String, line: Int, pos: Int) : OldError("Syntax Error", message, filename, line, pos)