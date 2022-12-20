package studio.karllang.karl.lexer

import studio.karllang.karl.errors.LexicalError

class Lexer(private val input: String) {
    val tokens = ArrayList<Token>()
    private val buffer: StringBuilder = StringBuilder()
    private val chars = "()[]{}^*=<>,!~&:+|./%?;-"
    private val operators: HashMap<String, TokenType> = HashMap()
    private val keywords: HashMap<String, TokenType> = HashMap()
    private var position = 0
    private var line = 1

    init {
        operators["+"] = TokenType.PLUS
        operators["++"] = TokenType.PLUSPLUS
        operators["--"] = TokenType.MINUSMINUS
        operators["&&"] = TokenType.AND
        operators["||"] = TokenType.OR
        operators["=="] = TokenType.EQUALEQUAL
        operators["!="] = TokenType.NOT_EQUAL
        operators[">"] = TokenType.GREATER
        operators["<"] = TokenType.LESS
        operators[">="] = TokenType.GREATER_EQUAL
        operators["<="] = TokenType.LESS_EQUAL
        operators["-"] = TokenType.MINUS
        operators["*"] = TokenType.MULTIPLY
        operators["/"] = TokenType.DIVIDE
        operators["%"] = TokenType.MODULO
        operators["="] = TokenType.EQUAL
        operators["("] = TokenType.LEFT_PARENTHESIS
        operators[")"] = TokenType.RIGHT_PARENTHESIS
        operators["["] = TokenType.LEFT_BRACKET
        operators["]"] = TokenType.RIGHT_BRACKET
        operators["{"] = TokenType.LEFT_BRACE
        operators["}"] = TokenType.RIGHT_BRACE
        operators[","] = TokenType.COMMA
        operators[":"] = TokenType.COLON
        operators["&"] = TokenType.AMP
        operators["|"] = TokenType.BAR
        operators["."] = TokenType.POINT
        operators["^"] = TokenType.POW
        operators["~"] = TokenType.TILDE
        operators["?"] = TokenType.QUESTION
        operators["!"] = TokenType.EXCLAMATION
        operators[";"] = TokenType.SEMICOLON
        operators["//"] = TokenType.COMMENTARY

        keywords["func"] = TokenType.FUNC
        keywords["return"] = TokenType.RETURN
        keywords["if"] = TokenType.IF
        keywords["else"] = TokenType.ELSE
        keywords["while"] = TokenType.WHILE
        keywords["for"] = TokenType.FOR
        keywords["bool"] = TokenType.BOOL
        keywords["final"] = TokenType.FINAL
        keywords["int"] = TokenType.INT
        keywords["float"] = TokenType.FLOAT
        keywords["string"] = TokenType.STRING
        keywords["char"] = TokenType.CHAR
        keywords["void"] = TokenType.VOID
        keywords["null"] = TokenType.NULL

        lex()
    }

    private fun lex() {
        while (position < input.length) {
            val c = input[position]
            if (c == '/' && position + 1 < input.length && input[position + 1] == '/') tokenizeComment() else if (c == '/' && position + 1 < input.length && input[position + 1] == '*') tokenizeMultiLineComment() else if (c == '\n' || c == '\r') {
                line++
                position++
            }
            else if (Character.isDigit(c) || c == '-' && position + 1 < input.length && Character.isDigit(input[position + 1])) tokenizeNumber()
            else if (c.toString().matches("^[a-zA-Z_$][a-zA-Z_$0-9]*$".toRegex())) tokenizeIdentifier()
            else if (c == '"') tokenizeString()
            else if (c == '\'') tokenizeChar()
            else if (chars.indexOf(c) != -1) tokenizeOperator()
            else if (Character.isWhitespace(c)) nextChar()
            throw LexicalError("Syntax Error", "Unexpected character: $c", line, getLine())
        }

        tokens.add(Token(TokenType.EOF, "EOF", input.length, line))
    }

    private fun tokenizeComment() {
        while (position < input.length && input[position] != '\n') {
            position++
        }
    }

    private fun tokenizeMultiLineComment() {
        while (position + 1 < input.length && !(input[position] == '*' && input[position + 1] == '/')) {
            position++
        }
        position += 2
    }

    private fun tokenizeChar() {
        nextChar()
        val c = input[position]
        nextChar()
        if (input[position] != '\'') {
            if (input[position] != '\'') {
                throw LexicalError("Syntax Error", "Character type can only contain one character", line, getLine())
            } else {
                throw LexicalError("Syntax Error", "Excepted ' at end of character", line, getLine())
            }
        }
        nextChar()
        tokens.add(Token(TokenType.CHAR_VALUE, c.toString(), position - 2, line))
    }

    private fun tokenizeNumber() {
        buffer.setLength(0)
        var c = input[position]
        if (position + 1 < input.length && Character.isLetter(input[position + 1])) {
            throw LexicalError("Syntax Error", "Unexpected character: $c", line, getLine())
        }
        while (true) {
            if (c == '\u0000') {
                break
            }
            if (c == '.' && buffer.indexOf(".") != -1 || c == '-' && buffer.indexOf("-") != -1) {
                throw LexicalError("Syntax Error", "Invalid number : $buffer", line, getLine())
            } else if (!Character.isDigit(c) && c != '.' && c != '-') {
                break
            }
            buffer.append(c)
            c = nextChar()
        }
        if (buffer.indexOf(".") != -1) {
            addToken(TokenType.FLOAT_VALUE, buffer.toString())
        } else {
            addToken(TokenType.INT_VALUE, buffer.toString())
        }
    }

    private fun tokenizeIdentifier() {
        buffer.setLength(0)
        var c = input[position]
        while (true) {
            if (c == '\u0000') {
                break
            }
            if (!Character.isLetterOrDigit(c) && c != '_') {
                break
            }
            buffer.append(c)
            c = nextChar()
        }
        if (buffer.toString() == "true" || buffer.toString() == "false") {
            addToken(TokenType.BOOL_VALUE, buffer.toString())
        } else if (buffer.toString() == "show") {
            addToken(TokenType.SHOW, buffer.toString())
        } else {
            addToken(keywords.getOrDefault(buffer.toString(), TokenType.IDENTIFIER), buffer.toString())
        }
    }

    private fun tokenizeString() {
        buffer.setLength(0)
        var c = nextChar()
        while (true) {
            if (c == '\\') {
                c = nextChar()
                when (c) {
                    'n' -> buffer.append('\n')
                    't' -> buffer.append('\t')
                    'r' -> buffer.append('\r')
                    'b' -> buffer.append('\b')
                    'f' -> buffer.append('\u000c')
                    '\'' -> buffer.append('\'')
                    '"' -> buffer.append('\"')
                    '\\' -> buffer.append('\\')
                    '0' -> buffer.append('\u0000')
                    else -> throw LexicalError("Syntax Error", "Invalid escape character: $c", line, getLine())
                }
                c = nextChar()
            }
            if (c == '\u0000') {
                throw LexicalError("Syntax Error", "Unterminated string", line, getLine())
            }
            if (c == '"') {
                nextChar()
                break
            }
            buffer.append(c)
            c = nextChar()
        }
        addToken(TokenType.STR_VALUE, buffer.toString())
    }

    private fun tokenizeOperator() {
        buffer.setLength(0)
        var c = input[position]
        while (true) {
            if (c == '\u0000') {
                break
            }

            if (chars.indexOf(c) == -1) {
                break
            }
            buffer.append(c)
            c = nextChar()
            if (operators.containsKey(buffer.toString())) {
                if (c.toString() == buffer.toString() && listOf('|', '&', '=', '+', '-', '/').contains(c)) {
                    addToken(operators[buffer.toString() + c]!!, buffer.toString() + c)
                    nextChar()
                } else if (listOf(">", "<", "!").contains(buffer.toString()) && c == '=') {
                    addToken(operators[buffer.toString() + c]!!, buffer.toString() + c)
                    nextChar()
                } else {
                    addToken(operators[buffer.toString()]!!, buffer.toString())
                }
                return
            }
        }
    }

    private fun nextChar(): Char {
        position++
        return if (position >= input.length) {
            '\u0000'
        } else input[position]
    }

    private fun addToken(type: TokenType, value: String) {
        tokens.add(Token(type, value, position, line))
    }

    private fun getLine(): String {
        val lines = input.split("\n", "\r")
        return if (lines.isNotEmpty()) lines[line - 1] else ""
    }
}