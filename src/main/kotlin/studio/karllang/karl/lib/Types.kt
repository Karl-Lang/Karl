package studio.karllang.karl.lib

import studio.karllang.karl.lexer.TokenType
import java.util.*

class Types {
    companion object {
        private val types = HashMap<String, TokenType>()
        private val values_types = HashMap<String, TokenType>()

        init {
            types["int"] = TokenType.INT
            types["string"] = TokenType.STRING
            types["bool"] = TokenType.BOOL
            types["float"] = TokenType.FLOAT
            types["char"] = TokenType.CHAR
            types["null"] = TokenType.NULL

            values_types["string"] = TokenType.STR_VALUE
            values_types["int"] = TokenType.INT_VALUE
            values_types["bool"] = TokenType.BOOL_VALUE
            values_types["float"] = TokenType.FLOAT_VALUE
            values_types["char"] = TokenType.CHAR_VALUE
        }

        @JvmStatic
        fun checkValueType(expectedType: TokenType?, type: TokenType): Boolean {
            return when (expectedType) {
                TokenType.INT -> type === TokenType.INT_VALUE
                TokenType.FLOAT -> type === TokenType.FLOAT_VALUE
                TokenType.STRING -> type === TokenType.STR_VALUE
                TokenType.CHAR -> type === TokenType.CHAR_VALUE
                TokenType.BOOL -> type === TokenType.BOOL_VALUE
                else -> false
            }
        }

        @JvmStatic
        fun getTypeName(type: TokenType): String {
            return when (type) {
                TokenType.INT_VALUE -> "int"
                TokenType.FLOAT_VALUE -> "float"
                TokenType.STR_VALUE -> "string"
                TokenType.CHAR_VALUE -> "char"
                TokenType.BOOL_VALUE -> "bool"
                else -> type.getName().lowercase(Locale.getDefault())
            }
        }

        @JvmStatic
        fun getType(name: String?): TokenType? {
            return types[name]
        }

        @JvmStatic
        fun isType(type: TokenType?): Boolean {
            return types.containsValue(type)
        }

        @JvmStatic
        operator fun contains(type: TokenType?): Boolean {
            return types.containsValue(type)
        }

        @JvmStatic
        fun isValueType(type: TokenType?): Boolean {
            return values_types.containsValue(type)
        }
    }
}