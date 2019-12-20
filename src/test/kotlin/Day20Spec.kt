
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals



object Day20Spec: Spek({

    given("AoC 19.20.1") {

        it("works with test data 1") {
            assertEquals(23, Day20.solvePt1(readLines("20_1.txt")))
        }

        it("works with test data 2") {
            assertEquals(58, Day20.solvePt1(readLines("20_2.txt")))
        }

        it("works with task data immutable") {
            assertEquals(454, Day20.solvePt1(readLines("20.txt")))
        }
    }

    given("AoC 19.20.2") {

        it("works with test data 1") {
            assertEquals(26, Day20.solvePt2(readLines("20_1.txt")))
        }

        it("works with test data 2") {
            assertEquals(396, Day20.solvePt2(readLines("20_3.txt")))
        }


        it("works with task data") {
            assertEquals(5744, Day20.solvePt2(readLines("20.txt")))
        }

    }
})