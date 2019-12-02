
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day2Spec: Spek({

    given("AoC 19.2.1") {

        it("works with test data") {
            assertEquals(2, Day2.doPt1(Day2.parseInput("1,0,0,0,99"), 0).first())
        }

        it("works with test data") {
            assertEquals(30, Day2.doPt1(Day2.parseInput("1,1,1,4,99,5,6,0,99"), 0).first())
        }

        it("works with task data") {
            assertEquals(3850704, Day2.solvePt1(Day2.parseInput(readLines("2.txt").first()), 12, 2).first())
        }
    }

    given("AoC 19.2.2") {

        it("works with task data") {
            assertEquals(6718, Day2.solvePt2(readLines("2.txt").first()))
        }

    }
})