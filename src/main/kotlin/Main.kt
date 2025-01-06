package org.alf3ratz

import CLexer
import CParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

const val FILE_PATH_ERROR_MSG = "Ошибка: необходимо указать путь к файлу после флага -file"
const val CODE_LINE_ERROR_MSG = "Ошибка: необходимо указать строку кода после флага -code"
const val CODE_LINES_ERROR_MSG = "Ошибка: необходимо указать строки кода после флага -lines"
const val UNSUPPORTED_FLAG_ERROR_MSG = "Неизвестный флаг: "
const val NO_CODE_ERROR_MSG = "Ошибка: код не был предоставлен"

fun main(args: Array<String>) {
    var inputCode = ""

    // Обрабатываем флаги
    var i = 0
    while (i < args.size) {
        when (args[i]) {
            "-file" -> {
                // Чтение из файла
                if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                    val filePath = args[i + 1]
                    inputCode = File(filePath).readText()
                    i++  // Пропускаем аргумент пути к файлу
                } else {
                    println(FILE_PATH_ERROR_MSG)
                    return
                }
            }

            "-code" -> {
                // Прямо строка кода
                if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                    inputCode = args[i + 1]
                    i++  // Пропускаем строку кода
                } else {
                    println(CODE_LINE_ERROR_MSG)
                    return
                }
            }

            "-lines" -> {
                // Несколько строк кода (через запятую)
                if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                    val lines = args[i + 1].split(",")
                    inputCode = lines.joinToString("\n")
                    i++  // Пропускаем строку с кодом
                } else {
                    println(CODE_LINES_ERROR_MSG)
                    return
                }
            }

            else -> {
                println("$UNSUPPORTED_FLAG_ERROR_MSG${args[i]}")
                return
            }
        }
        i++
    }

    if (inputCode.isEmpty()) {
        println(NO_CODE_ERROR_MSG)
        return
    }

    // Создание лексера и парсера для C
    val input = CharStreams.fromString(inputCode)
    val lexer = CLexer(input)  // CLexer — это лексер для C, сгенерированный ANTLR
    val tokens = CommonTokenStream(lexer)
    val parser = CParser(tokens)  // CParser — это парсер для C, сгенерированный ANTLR

    // Получаем синтаксическое дерево
    val tree = parser.program()

    // Печать синтаксического дерева для отладки
    println("Синтаксическое дерево:")
    println(tree.toStringTree(parser))

    // Создание экземпляра транслятора для Kotlin
    val visitor = KotlinTranslator()
    val kotlinCode = visitor.visit(tree)

    // Вывод сгенерированного кода на Kotlin
    println("\nСгенерированный код на Kotlin:")
    println(kotlinCode)
}
