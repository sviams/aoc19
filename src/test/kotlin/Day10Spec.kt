
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day10Spec: Spek({

    given("AoC 19.10.1") {

        it("works with test data") {
            assertEquals(8, Day10.solvePt1(readLines("10_ex.txt")))
        }

        it("works with test data 1") {
            assertEquals(33, Day10.solvePt1(readLines("10_1.txt")))
        }

        it("works with test data 2") {
            assertEquals(35, Day10.solvePt1(readLines("10_2.txt")))
        }

        it("works with test data 3") {
            assertEquals(41, Day10.solvePt1(readLines("10_3.txt")))
        }

        it("works with test data 4") {
            assertEquals(210, Day10.solvePt1(readLines("10_4.txt")))
        }

        it("works with task data") {
            assertEquals(214, Day10.solvePt1(readLines("10.txt")))
        }
    }

    given("AoC 19.10.2") {

        it("works with test data") {
            assertEquals(802, Day10.solvePt2(readLines("10_4.txt"), Day10.Pos(11,13)))
        }
        /*
        it("works with test data 2") {
            assertEquals(-1, Day10.solvePt2(readLines("10_2_1.txt"), Day10.Pos(8,4)))
        }
        */

        it("works with task data") {
            assertEquals(502, Day10.solvePt2(readLines("10.txt"), Day10.Pos(8,16)))
        }

    }
})