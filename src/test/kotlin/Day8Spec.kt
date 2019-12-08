
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day8Spec: Spek({

    given("AoC 19.8.1") {

        it("works with test data") {
            assertEquals(1, Day8.solvePt1(3,2, listOf("123456789012").first().toCharArray().map { it.toInt() - 48 }))
        }

        it("works with task data") {
            assertEquals(1088, Day8.solvePt1(25,6, readLines("8.txt").first().toCharArray().map { it.toInt() - 48 }))
        }
    }

    given("AoC 19.8.2") {

        it("works with test data") {
            assertEquals("LGYHB", Day8.solvePt2(2,2,listOf("0222112222120000").first().toCharArray().map { it.toInt() - 48 }))
        }

        it("works with task data") {
            assertEquals("LGYHB", Day8.solvePt2(25,6, readLines("8.txt").first().toCharArray().map { it.toInt() - 48 }))
        }

    }
})