package org.alf3ratz.util

import CLexer
import CParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

class Utils {
    fun getAstTree(inputCode: String): CParser.ProgramContext {
        val input = CharStreams.fromString(inputCode)
        val lexer = CLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = CParser(tokens)
        return parser.program()
    }
}