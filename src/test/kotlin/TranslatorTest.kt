import org.alf3ratz.KotlinTranslator
import org.alf3ratz.util.Utils
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import kotlin.test.assertEquals


class TranslatorTest {
    val visitor: KotlinTranslator = KotlinTranslator()

    @Test
    fun declarationTest() {
        val declaration = "int x = 5;"
        val tree = Utils().getAstTree(declaration)
        val kotlinCode = visitor.visit(tree)
        assertEquals("Int x = 5", kotlinCode)
    }

    @ParameterizedTest
    @ValueSource(strings = [">", "<", "==", "!=", "&&", "||"])
    fun logicOperatorTest(operator: String) {
        val declaration = "int x = 5 $operator 6;"
        val tree = Utils().getAstTree(declaration)
        val kotlinCode = visitor.visit(tree)
        assertEquals("Int x = (5 $operator 6)", kotlinCode)
    }

    @Test
    fun whileLoopTest() {
        val declaration = " int z = 5; while(true){ int x = 5; }"
        val tree = Utils().getAstTree(declaration)
        val kotlinCode = visitor.visit(tree)
        assertEquals(
            "Int z = 5\n" +
                    "while(true){Int x = 5}", kotlinCode
        )
    }

    @ParameterizedTest
    @MethodSource("typeArguments")
    fun blockTest(cType: String, kType: String) {
        val codeBlock = "{$cType x = true;}"
        val tree = Utils().getAstTree(codeBlock)
        val kotlinCode = visitor.visit(tree)
        println(kotlinCode)
        assertEquals(
            "{\n$kType x = true\n}", kotlinCode
        )
    }

    companion object {
        @JvmStatic
        private fun typeArguments(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("int", "Int"),
                Arguments.of("boolean", "Boolean"),
                Arguments.of("double", "Double"),
                Arguments.of("float", "Float")
            )
        }
    }
}