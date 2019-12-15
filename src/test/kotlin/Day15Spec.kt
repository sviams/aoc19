
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals



object Day15Spec: Spek({

    given("AoC 19.15.1") {

        it("works with task data immutable") {
            assertEquals(404, Day15.solvePt1(readLines("15.txt").first().split(",").map { it.toLong() }))
        }
    }

    given("AoC 19.15.2") {

        it("works with task data immutable") {
            assertEquals(406, Day15.solvePt2(readLines("15.txt").first().split(",").map { it.toLong() }))
        }

    }
})