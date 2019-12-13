
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals



object Day13Spec: Spek({

    given("AoC 19.13.1") {

        it("works with task data immutable") {
            assertEquals(200, Day13.solvePt1(readLines("13.txt").first().split(",").map { it.toLong() }))
        }
    }

    given("AoC 19.13.2") {

        it("works with task data immutable") {
            assertEquals(9803, Day13.solvePt2(readLines("13.txt").first().split(",").map { it.toLong() }))
        }

    }
})