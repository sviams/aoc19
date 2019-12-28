
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object DayZZZSpec: Spek({

    given("AoC 19.ZZZ.1") {

        it("works with test data") {
            assertEquals(0, DayZZZ.solvePt1(listOf()))
        }

        it("works with test data 2") {
            assertEquals(0, DayZZZ.solvePt1(listOf()))
        }

        it("works with task data") {
            assertEquals(0, DayZZZ.solvePt1(readLines("ZZZ.txt")))
        }
    }

    given("AoC 19.ZZZ.2") {

        it("works with test data") {
            assertEquals(0, DayZZZ.solvePt2(listOf()))
        }

        it("works with task data") {
            assertEquals(0, DayZZZ.solvePt2(readLines("ZZZ.txt")))
        }

    }
})