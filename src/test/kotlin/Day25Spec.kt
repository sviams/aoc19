
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals



object Day25Spec: Spek({

    given("AoC 19.25.1") {

        it("works with task data") {
            assertEquals(352325632, Day25.solvePt1(IntCodeComputer.parse(readLines("25.txt").first().split(",").map { it.toLong() })))
        }
    }
})