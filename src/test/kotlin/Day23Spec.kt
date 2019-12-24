
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals



object Day23Spec: Spek({

    given("AoC 19.23.1") {

        it("works with task data") {
            assertEquals(27846, Day23.solvePt1(IntCodeComputer.parse(readLines("23.txt").first().split(",").map { it.toLong() })))
        }
    }

    given("AoC 19.23.2") {

        it("works with task data") {
            assertEquals(19959, Day23.solvePt2(IntCodeComputer.parse(readLines("23.txt").first().split(",").map { it.toLong() })))
        }

    }
})