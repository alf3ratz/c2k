package org.alf3ratz

import CBaseVisitor

class KotlinTranslator : CBaseVisitor<String>() {

    // Преобразуем объявление переменной из C в Kotlin
    override fun visitDeclaration(ctx: CParser.DeclarationContext): String {
        val type = ctx.type().text  // 'int', 'float', и т.д.
        val identifier = ctx.IDENTIFIER().text  // имя переменной

        // Преобразуем тип C в Kotlin
        val kotlinType = convertToKotlinType(type)

        return "$kotlinType $identifier = 0"  // Пример: "Int x = 0"
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