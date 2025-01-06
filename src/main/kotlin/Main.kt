package org.alf3ratz

import CLexer
import CParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun main() {
    // Входной код на C
    val inputCode = "int x = 5;"

    // Создание лексера и парсера для C
    val input = CharStreams.fromString(inputCode)
    val lexer = CLexer(input)  // CLexer — это лексер для C, сгенерированный ANTLR
    val tokens = CommonTokenStream(lexer)
    val parser = CParser(tokens)  // CParser — это парсер для C, сгенерированный ANTLR

    // Получаем синтаксическое дерево
    val tree = parser.program()

    // Печать синтаксического дерева для отладки
    println(tree.toStringTree(parser))

    // Создание экземпляра транслятора для Kotlin
    val visitor = KotlinTranslator()
    val kotlinCode = visitor.visit(tree)

    // Вывод сгенерированного кода на Kotlin
    println(kotlinCode)
}