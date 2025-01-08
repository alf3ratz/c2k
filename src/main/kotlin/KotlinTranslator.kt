package org.alf3ratz

import CBaseVisitor
import CParser

class KotlinTranslator : CBaseVisitor<String>() {

    // Преобразуем объявление переменной из C в Kotlin
    override fun visitDeclaration(ctx: CParser.DeclarationContext): String {
        val type = ctx.type().text  // 'int', 'float', и т.д.
        val identifier = ctx.IDENTIFIER().text  // имя переменной
        val expression = visit(ctx.expression())  // Преобразуем выражение
        val kotlinType = convertToKotlinType(type)

        return "$kotlinType $identifier = $expression"  // Пример: "Int x = 5"
    }

    // Преобразуем присваивание из C в Kotlin
    override fun visitAssignment(ctx: CParser.AssignmentContext): String {
        val identifier = ctx.IDENTIFIER().text
        val expression = visit(ctx.expression())  // Преобразуем выражение

        return "$identifier = $expression"
    }

    // Преобразуем выражение из C в Kotlin
    override fun visitExpression(ctx: CParser.ExpressionContext): String {
        return when {
            ctx.IDENTIFIER() != null -> ctx.IDENTIFIER().text  // Пример: "x"
            ctx.NUMBER() != null -> ctx.NUMBER().text  // Пример: "5"
            ctx.BOOLEAN() != null -> ctx.BOOLEAN().text  // Пример: "true"
            ctx.operator() != null -> {
                val left = visit(ctx.expression(0))
                val op = ctx.operator().text
                val right = visit(ctx.expression(1))
                "($left $op $right)"  // Пример: "(x && y)"
            }
            ctx.expression().size == 1 -> {
                val inner = visit(ctx.expression(0))
                "!$inner"  // Пример: "!x"
            }
            else -> visit(ctx.expression(0))  // Если выражение в скобках
        }
    }

    // Обработка блока кода
    override fun visitBlock(ctx: CParser.BlockContext): String {
        val statements = ctx.statement().joinToString("\n") { visit(it) }
        return "{\n$statements\n}"
    }

    // Обработка программы
    override fun visitProgram(ctx: CParser.ProgramContext): String {
        return ctx.statement().joinToString("\n") { visit(it) }
    }

    // Преобразование типов данных из C в Kotlin
    private fun convertToKotlinType(type: String): String {
        return when (type) {
            "int" -> "Int"
            "float" -> "Float"
            "double" -> "Double"
            "boolean" -> "Boolean"
            else -> type
        }
    }

}
