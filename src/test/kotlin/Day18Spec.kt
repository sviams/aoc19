
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals



object Day18Spec: Spek({

    given("AoC 19.18.1") {

        it("works with test data 1") {
            assertEquals(8, Day18.solvePt1(readLines("18_1.txt")))
        }

        it("works with test data 2") {
            assertEquals(86, Day18.solvePt1(readLines("18_2.txt")))
        }

        it("works with test data 3") {
            assertEquals(132, Day18.solvePt1(readLines("18_3.txt")))
        }

        it("works with test data 4") {
            assertEquals(136, Day18.solvePt1(readLines("18_4.txt")))
        }

        it("works with test data 5") {
            assertEquals(81, Day18.solvePt1(readLines("18_5.txt")))
        }

        it("works with task data immutable") {
            assertEquals(4770, Day18.solvePt1(readLines("18.txt")))
        }
    }

    given("AoC 19.18.2") {

        it("works with test data 6") {
            assertEquals(72, Day18.solvePt2(readLines("18_6.txt")))
        }

        it("works with test data 7") {
            assertEquals(32, Day18.solvePt2(readLines("18_7.txt")))
        }

        it("works with test data 8") {
            assertEquals(8, Day18.solvePt2(readLines("18_8.txt")))
        }

        it("works with test data 9") {
            assertEquals(24, Day18.solvePt2(readLines("18_9.txt")))
        }

        it("works with task data") {
            assertEquals(1578, Day18.solvePt2(readLines("18_pt2.txt")))
        }

    }
})