package org.alf3ratz

import CBaseVisitor
import CParser

class KotlinTranslator : CBaseVisitor<String>() {

    override fun visitDeclaration(ctx: CParser.DeclarationContext): String {
        val type = ctx.type().text
        val identifier = ctx.IDENTIFIER().text
        val expression = visit(ctx.expression())
        val kotlinType = convertToKotlinType(type)

        return "$kotlinType $identifier = $expression"
    }

    override fun visitAssignment(ctx: CParser.AssignmentContext): String {
        val identifier = ctx.IDENTIFIER().text
        val expression = visit(ctx.expression())

        return "$identifier = $expression"
    }

    override fun visitExpression(ctx: CParser.ExpressionContext): String {
        return when {
            ctx.IDENTIFIER() != null -> ctx.IDENTIFIER().text
            ctx.NUMBER() != null -> ctx.NUMBER().text
            ctx.BOOLEAN() != null -> ctx.BOOLEAN().text
            ctx.operator() != null -> {
                val left = visit(ctx.expression(0))
                val op = ctx.operator().text
                val right = visit(ctx.expression(1))
                "($left $op $right)"  // Пример: "(x && y)"
            }

            ctx.expression().size == 1 -> {
                val inner = visit(ctx.expression(0))
                "!$inner"
            }

            else -> visit(ctx.expression(0))
        }
    }

    override fun visitBlock(ctx: CParser.BlockContext): String {
        val statements = ctx.statement().joinToString("\n") { visit(it) }
        return "{\n$statements\n}"
    }

    override fun visitProgram(ctx: CParser.ProgramContext): String {
        return ctx.statement().joinToString("\n") { visit(it) }
    }

    override fun visitWhileLoop(ctx: CParser.WhileLoopContext): String {
        val expression = visit(ctx.expression())
        val statements = ctx.statement().joinToString("\n") { visit(it) }
        return "while($expression){$statements}"
    }

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
