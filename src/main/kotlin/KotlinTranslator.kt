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
//        val identifier = ctx.IDENTIFIER().text
//        val expression = visit(ctx.expression())

        // return "$identifier = $expression"
        return ""
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
                "($left $op $right)"
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

    override fun visitForLoop(ctx: CParser.ForLoopContext): String {
        val initialization = ctx.declaration() ?: ctx.assignment() ?: ctx.expression(0)

        val start = when (initialization) {
            is CParser.DeclarationContext -> ctx.declaration().IDENTIFIER().text
            is CParser.AssignmentContext -> ctx.assignment().IDENTIFIER().text
            else -> ""
        }

        val startDeclaration = when (initialization) {
            is CParser.DeclarationContext -> visit(initialization)
            is CParser.AssignmentContext -> visit(initialization)
            else -> ""
        }

        val condition = if (ctx.expression(0) != null) {
            visit(ctx.expression(0))
        } else {
            "true"
        }

        val step = if (ctx.expression(1) != null) {
            visit(ctx.expression(1))  // инкремент или декремент
        } else {
            ""
        }

        val range = when {
            start.isNotEmpty() && condition.contains("<") -> {
                val subString = condition.slice(condition.indexOf('<') + 1 until condition.length - 1)
                val endOfLoopValue = subString.trim().toInt() - 1
                "${startDeclaration.substringAfter("=").trim()}..${endOfLoopValue}"
            }

            else -> ""
        }

        val statements = ctx.statement().joinToString("\n") { visit(it) }

        return if (range.isNotEmpty()) {
            "for ($start in $range) {\n$statements\n}"
        } else {
            "for ($start; $condition; $step) {\n$statements\n}"
        }
    }

    override fun visitArrayDeclaration(ctx: CParser.ArrayDeclarationContext): String {
        return super.visitArrayDeclaration(ctx)
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
