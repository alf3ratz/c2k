package org.alf3ratz

import org.alf3ratz.util.Utils
import java.io.File

const val FILE_PATH_ERROR_MSG = "Ошибка: необходимо указать путь к файлу после флага -file"
const val CODE_LINE_ERROR_MSG = "Ошибка: необходимо указать строку кода после флага -code"
const val CODE_LINES_ERROR_MSG = "Ошибка: необходимо указать строки кода после флага -lines"
const val UNSUPPORTED_FLAG_ERROR_MSG = "Неизвестный флаг: "
const val NO_CODE_ERROR_MSG = "Ошибка: код не был предоставлен"

fun main(args: Array<String>) {
    var inputCode = ""

    var i = 0
    while (i < args.size) {
        when (args[i]) {
            "-file" -> {
                if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                    val filePath = args[i + 1]
                    inputCode = File(filePath).readText()
                    i++
                } else {
                    println(FILE_PATH_ERROR_MSG)
                    return
                }
            }

            "-code" -> {
                if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                    inputCode = args[i + 1]
                    i++
                } else {
                    println(CODE_LINE_ERROR_MSG)
                    return
                }
            }

            "-lines" -> {
                if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                    val lines = args[i + 1].split(",")
                    inputCode = lines.joinToString("\n")
                    i++
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

    val tree = Utils().getAstTree(inputCode)

    val visitor = KotlinTranslator()
    val kotlinCode = visitor.visit(tree)

    println("\nСгенерированный код на Kotlin:")
    println(kotlinCode)
}
