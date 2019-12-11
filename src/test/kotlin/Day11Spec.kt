
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day11Spec: Spek({

    given("AoC 19.11.1") {

        it("works with task data immutable") {
            assertEquals(2088, Day11.solvePt1(readLines("11.txt").first().split(",").map { it.toLong() }))
        }
    }

    given("AoC 19.11.2") {

        it("works with task data immutable") {
            assertEquals("URCAFLCP", Day11.solvePt2(readLines("11.txt").first().split(",").map { it.toLong() }))
        }

    }
})