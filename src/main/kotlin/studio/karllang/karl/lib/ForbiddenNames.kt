package studio.karllang.karl.lib

class ForbiddenNames {
    companion object {
        private val FORBIDDEN_VARIABLE_NAMES = arrayOf(
            "string",
            "int",
            "float",
            "double",
            "bool",
            "char",
            "void",
            "null",
            "true",
            "false",
            "if",
            "else",
            "while",
            "for",
            "do",
            "switch",
            "case",
            "default",
            "break",
            "continue",
            "return",
            "new",
            "this",
            "super",
            "class",
            "interface",
            "package",
            "import",
            "public",
            "private",
            "protected",
            "static",
            "final",
            "abstract",
            "native",
            "synchronized",
            "transient",
            "volatile",
            "strictfp",
            "extends",
            "implements",
            "instanceof",
            "try",
            "catch",
            "finally",
            "throw",
            "throws",
            "assert",
            "enum",
            "goto",
            "const",
            "byte",
            "short",
            "long",
            "true",
            "false",
            "null",
            "void",
            "eval"
        )

        private val FORBIDDEN_FUNCTION_NAMES = arrayOf(
            "show",
            "eval"
        )

        @JvmStatic
        fun isForbiddenName(name: String): Boolean {
            if (FORBIDDEN_VARIABLE_NAMES.contains(name)) return true
            return FORBIDDEN_FUNCTION_NAMES.contains(name)
        }
    }
}
