package dk.lessor

import dk.lessor.TokenType.*

fun main() {
    val lines = readFile("day_18.txt").map { it.replace(" ", "") }
    println(simpleMathCalculator(lines))
    println(advancedMathCalculator(lines))
}

fun simpleMathCalculator(lines: List<String>): Long {
    return lines.map { Interpreter(Parser(Scanner(it).scanTokens()).parse()).interpret() as Long }.sum()
}

fun advancedMathCalculator(lines: List<String>): Long {
    return lines.map { Interpreter(AdvancedParser(Scanner(it).scanTokens()).parse()).interpret() as Long }.sum()
}

class Scanner(private val source: String) {
    private val tokens = mutableListOf<Token>()
    private var start = 0
    private var current = 0

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            when (val c = advance()) {
                '(' -> addToken(LEFT_PAREN)
                ')' -> addToken(RIGHT_PAREN)
                '*' -> addToken(STAR)
                '/' -> addToken(SLASH)
                '+' -> addToken(PLUS)
                '-' -> addToken(MINUS)
                else -> {
                    if (c.isDigit()) {
                        number()
                    } else {
                        throw RuntimeException("Unrecognized token $c at position $current")
                    }
                }
            }
        }

        tokens.add(Token(EOF))

        return tokens
    }

    private fun isAtEnd(): Boolean = current >= source.length

    private fun advance(): Char = source[current++]

    private fun peek(): Char = if (isAtEnd()) '\u0000' else source[current]

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text = source.substring(start until current)
        tokens.add(Token(type, text, literal))
    }

    private fun number() {
        while (peek().isDigit()) advance()

        addToken(NUMBER, source.substring(start until current).toLong())
    }
}

open class Parser(private val tokens: List<Token>) {
    private var current = 0

    fun parse(): Expr {
        return expression()
    }

    protected open fun expression(): Expr {
        return term()
    }

    protected open fun term(): Expr {
        var expr = primary()

        while (match(MINUS, PLUS, SLASH, STAR)) {
            val operator = previous()
            val right = primary()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    protected fun primary(): Expr {
        if (match(NUMBER)) {
            return Expr.Literal(previous().literal!!)
        }

        if (match(LEFT_PAREN)) {
            val expr = expression()
            consume(RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }

        throw RuntimeException("Could not match any primary token")
    }

    protected fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }

        return false
    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return false
        return peek().type == type
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd() = peek().type == EOF

    private fun peek() = tokens[current]

    protected fun previous() = tokens[current - 1]

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw RuntimeException("${peek()} - $message")
    }
}

class AdvancedParser(tokens: List<Token>) : Parser(tokens) {
    override
    fun expression(): Expr {
        return factor()
    }

    override fun term(): Expr {
        var expr = primary()

        while (match(MINUS, PLUS)) {
            val operator = previous()
            val right = primary()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun factor(): Expr {
        var expr = term()

        while (match(SLASH, STAR)) {
            val operator = previous()
            val right = term()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }
}

class Interpreter(private val expression: Expr) : Expr.Visitor<Any> {
    fun interpret(): Any {
        return evaluate(expression)
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any {
        val left = evaluate(expr.left) as Long
        val right = evaluate(expr.right) as Long

        return when (expr.operator.type) {
            MINUS -> left - right
            SLASH -> left / right
            STAR -> left * right
            PLUS -> left + right
            else -> throw RuntimeException("Unknown binary expression: $expr")
        }
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any {
        return evaluate(expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any {
        return expr.value
    }

    private fun evaluate(expr: Expr): Any {
        return expr.accept(this)
    }

}

data class Token(
    val type: TokenType,
    val lexeme: String = "",
    val literal: Any? = null,
) {
    override fun toString(): String {
        return "$type $lexeme $literal"
    }
}

enum class TokenType {
    LEFT_PAREN,
    RIGHT_PAREN,
    STAR,
    SLASH,
    PLUS,
    MINUS,
    NUMBER,
    EOF
}


sealed class Expr {
    abstract fun <R> accept(visitor: Visitor<R>): R

    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBinaryExpr(this)
        }
    }

    data class Grouping(val expression: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitGroupingExpr(this)
        }
    }

    data class Literal(val value: Any) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLiteralExpr(this)
        }
    }

    interface Visitor<R> {
        fun visitBinaryExpr(expr: Binary): R
        fun visitGroupingExpr(expr: Grouping): R
        fun visitLiteralExpr(expr: Literal): R
    }
}