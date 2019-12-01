
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day1Spec: Spek({

    given("AoC 19.1.1") {

        it("works with test data") {
            assertEquals(33583, Day1.solvePt1(listOf(100756)))
        }

        it("works with task data") {
            assertEquals(3255932, Day1.solvePt1(readLines("1.txt").map { it.toInt() }))
        }
    }

    given("AoC 19.1.2") {

        it("works with test data") {
            assertEquals(50346, Day1.solvePt2(listOf(100756)))
        }

        it("works with task data") {
            assertEquals(4881041, Day1.solvePt2(readLines("1.txt").map { it.toInt() }))
        }

    }
})