package studio.karllang.karl.errors.runtime

open class RuntimeError(
    private val msg: String,
    private val pos: Int,
    private val line: Int,
    private val lineString: String
) : Throwable() {
    fun getMsg(): String {
        return msg
    }

    fun getPosition(): Int {
        return pos
    }

    fun getLine(): Int {
        return line
    }

    fun getLineString(): String {
        return lineString
    }
}