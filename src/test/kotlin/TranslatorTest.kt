import org.alf3ratz.KotlinTranslator
import org.alf3ratz.Utils
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class TranslatorTest {

    @Test
    fun declarationTest() {
        val declaration = "int x = 5;"
        val tree = Utils().getAstTree(declaration)
        val visitor = KotlinTranslator()
        val kotlinCode = visitor.visit(tree)
        assertEquals("Int x = 5", kotlinCode)
    }

    @ParameterizedTest
    @ValueSource(strings = [">", "<", "==", "!=", "&&", "||"])
    fun logicOperatorTest(operator: String) {
        val declaration = "int x = 5 $operator 6;"
        val tree = Utils().getAstTree(declaration)
        val visitor = KotlinTranslator()
        val kotlinCode = visitor.visit(tree)
        assertEquals("Int x = (5 $operator 6)", kotlinCode)
    }
}