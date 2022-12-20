package studio.karllang.karl.olderrors.runtime

import studio.karllang.karl.olderrors.OldError


class NumberOldError(message: String, fileName: String, line: Int, position: Int) : OldError("Number Error", message, fileName, line, position)