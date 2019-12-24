
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day24Spec: Spek({

    given("AoC 19.24.1") {

        it("works with test data") {
            assertEquals(2129920, Day24.solvePt1(readLines("24_1.txt")))
        }

        it("works with task data") {
            assertEquals(18401265, Day24.solvePt1(readLines("24.txt")))
        }
    }

    given("AoC 19.24.2") {

        it("works with test data") {
            assertEquals(99, Day24.solvePt2(readLines("24_1.txt"), 10))
        }

        it("works with task data") {
            assertEquals(2078, Day24.solvePt2(readLines("24.txt"), 200))
        } // low

    }
})