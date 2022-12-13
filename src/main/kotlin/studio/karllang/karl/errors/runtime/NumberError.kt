package studio.karllang.karl.errors.runtime

import studio.karllang.karl.errors.Error


class NumberError(message: String, fileName: String, line: Int, position: Int) : Error("Number Error", message, fileName, line, position)