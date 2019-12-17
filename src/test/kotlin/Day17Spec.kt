
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals



object Day17Spec: Spek({

    given("AoC 19.17.1") {

        it("works with task data immutable") {
            assertEquals(7404, Day17.solvePt1(readLines("17.txt").first().split(",").map { it.toLong() }))
        }
    }

    given("AoC 19.17.2") {

        it("works with task data immutable") {
            assertEquals(929045, Day17.solvePt2(readLines("17.txt").first().split(",").map { it.toLong() }))
        }

    }
})