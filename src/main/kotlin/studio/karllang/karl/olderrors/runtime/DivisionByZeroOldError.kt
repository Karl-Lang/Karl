package studio.karllang.karl.olderrors.runtime

class DivisionByZeroOldError(fileName: String, line: Int, pos: Int) : RuntimeOldError( "Division by zero", fileName, line, pos)