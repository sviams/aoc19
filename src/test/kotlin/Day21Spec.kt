
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals



object Day21Spec: Spek({

    given("AoC 19.21.1") {

        it("works with task data") {
            assertEquals(19354890, Day21.solvePt1(IntCodeComputer.parse(readLines("21.txt").first().split(",").map { it.toLong() })))
        }
    }

    given("AoC 19.21.2") {

        it("works with task data") {
            assertEquals(1140664209, Day21.solvePt2(IntCodeComputer.parse(readLines("21.txt").first().split(",").map { it.toLong() })))
        }

    }
})