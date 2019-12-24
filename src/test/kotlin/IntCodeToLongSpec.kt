
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals



object DayXYZSpec: Spek({

    given("AoC 19.XYZ.1") {

        it("works with test data") {
            assertEquals(-1, DayXYZ.solvePt1(IntCodeComputer.parse(readLines("XYZ.txt").first().split(",").map { it.toLong() })))
        }

        it("works with task data") {
            assertEquals(-1, DayXYZ.solvePt1(IntCodeComputer.parse(readLines("XYZ.txt").first().split(",").map { it.toLong() })))
        }
    }

    given("AoC 19.XYZ.2") {

        it("works with task data") {
            assertEquals(-1, DayXYZ.solvePt2(IntCodeComputer.parse(readLines("XYZ.txt").first().split(",").map { it.toLong() })))
        }

    }
})