
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day5Spec: Spek({

    given("AoC 19.5.1") {

        it("works with task data") {
            assertEquals(6745903, Day5.solvePt1(readLines("5.txt").first().split(",").map { it.toLong() }))
        }
    }

    given("AoC 19.5.2") {

        it("works with task data") {
            assertEquals(9168267, Day5.solvePt2(readLines("5.txt").first().split(",").map { it.toLong() }))
        }

    }
})