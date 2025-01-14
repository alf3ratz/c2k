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
    fun logicInnerOperatorTest() {
        val declaration = "boolean x = !true;"
        val tree = Utils().getAstTree(declaration)
        val kotlinCode = visitor.visit(tree)
        assertEquals("Boolean x = !true", kotlinCode)
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

    @Test
    fun forLoopTest() {
        val forLoop = "for(int i = 0; i< 5; i++){int x = 2;}"
        val tree = Utils().getAstTree(forLoop)
        val kotlinCode = visitor.visit(tree)
        assertEquals(
            "for (i in 0..4) {\nInt x = 2\n}", kotlinCode
        )
    }

    @ParameterizedTest
    @MethodSource("typeArguments")
    fun blockTest(cType: String, kType: String) {
        val codeBlock = "{$cType x = true;}"
        val tree = Utils().getAstTree(codeBlock)
        val kotlinCode = visitor.visit(tree)
        assertEquals(
            "{\n$kType x = true\n}", kotlinCode
        )
    }

    @Test
    fun arrayInitializationTest() {
        val array = "int arr[5] = {1, 2, 3, 4, 5};"
        val tree = Utils().getAstTree(array)
        val kotlinCode = visitor.visit(tree)
        assertEquals("val arr = arrayOf(1, 2, 3, 4, 5)", kotlinCode)
    }

    @Test
    fun matrixArrayTest() {
        val array = "int matrix[3][3];"
        val tree = Utils().getAstTree(array)
        val kotlinCode = visitor.visit(tree)
        assertEquals("val matrix = Array(3) { Array(3) { 0 } }", kotlinCode)
    }

    @Test
    fun arrayDeclarationTest() {
        val array = "int arr[5];"
        val tree = Utils().getAstTree(array)
        val kotlinCode = visitor.visit(tree)
        assertEquals("val arr = Array(5) { 0 }", kotlinCode)
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