
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals



object Day19Spec: Spek({

    given("AoC 19.19.1") {

        it("works with test data") {
            assertEquals(7, Day19.solvePt1(readLines("19.txt").first().split(",").map { it.toLong() }, 10, 10))
        }

        it("works with task data immutable") {
            assertEquals(126, Day19.solvePt1(readLines("19.txt").first().split(",").map { it.toLong() }, 50, 50))
        }
    }

    given("AoC 19.19.2") {

        it("works with task data immutable") {
            assertEquals(11351625, Day19.solvePt2(readLines("19.txt").first().split(",").map { it.toLong() }))
        }

    }
})