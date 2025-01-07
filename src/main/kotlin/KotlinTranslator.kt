package org.alf3ratz

import CBaseVisitor
import CParser

class KotlinTranslator : CBaseVisitor<String>() {

    // Преобразуем объявление переменной из C в Kotlin
    override fun visitDeclaration(ctx: CParser.DeclarationContext): String {
        val type = ctx.type().text  // 'int', 'float', и т.д.
        val identifier = ctx.IDENTIFIER().text  // имя переменной
        val resultException = visit(ctx.expression())
        // Преобразуем тип C в Kotlin
        val kotlinType = convertToKotlinType(type)

        return "$kotlinType $identifier = $resultException"  // Пример: "Int x = 0"
    }

    // Преобразуем присваивание из C в Kotlin
    override fun visitAssignment(ctx: CParser.AssignmentContext): String {
        val identifier = ctx.IDENTIFIER().text
        val expression = visit(ctx.expression())  // Преобразуем выражение

        return "$identifier = $expression"
    }

    // Преобразуем выражение (переменная или число) из C в Kotlin
    override fun visitExpression(ctx: CParser.ExpressionContext): String {
        return when {
            ctx.IDENTIFIER() != null -> ctx.IDENTIFIER().text  // Пример: "x"
            else -> ctx.NUMBER().text  // Пример: "5"
        }
    }

    // Обработка блока с фигурными скобками (например, тела функций или условных блоков)
    override fun visitBlock(ctx: CParser.BlockContext): String {
        // Каждый statement внутри блока обрабатывается отдельно
        val statements = ctx.statement().joinToString("\n") { visit(it) }
        // Возвращаем блок как строку с фигурными скобками и вложенными выражениями
        return "{\n$statements\n}"
    }

    // Преобразуем программу с учетом блока
    override fun visitProgram(ctx: CParser.ProgramContext): String {
        // Обрабатываем все операторы программы (включая блоки)
        return ctx.statement().joinToString("\n") { visit(it) }
    }

    // Преобразуем типы данных из C в Kotlin
    private fun convertToKotlinType(type: String): String {
        return when (type) {
            "int" -> "Int"
            "float" -> "Float"
            "double" -> "Double"
            else -> type  // Для остальных типов возвращаем как есть
        }
    }
}
