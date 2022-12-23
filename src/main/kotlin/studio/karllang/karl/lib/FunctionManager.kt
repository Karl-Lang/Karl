package studio.karllang.karl.lib

class FunctionManager {
    companion object {
        private val functions = HashMap<String, Function>()

        @JvmStatic
        fun addFunction(function: Function) {
            functions[function.getName()] = function
        }

        @JvmStatic
        fun getFunction(name: String): Function? {
            return functions[name]
        }

        @JvmStatic
        fun isFunction(name: String): Boolean {
            return functions.containsKey(name)
        }

        @JvmStatic
        fun clear() {
            functions.clear()
        }
    }
}
