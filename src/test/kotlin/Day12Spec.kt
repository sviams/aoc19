
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day12Spec: Spek({

    given("AoC 19.12.1") {

        it("works with test data") {
            assertEquals(179, Day12.solvePt1(listOf(
                    "<x=-1, y=0, z=2>",
                    "<x=2, y=-10, z=-7>",
                    "<x=4, y=-8, z=8>",
                    "<x=3, y=5, z=-1>"), 10))
        }

        it("works with test data 2") {
            assertEquals(1940, Day12.solvePt1(listOf(
                    "<x=-8, y=-10, z=0>",
                    "<x=5, y=5, z=10>",
                    "<x=2, y=-7, z=3>",
                    "<x=9, y=-8, z=-3>"), 100))
        }

        it("works with task data") {
            assertEquals(7077, Day12.solvePt1(readLines("12.txt"), 1000))
        }
    }

    given("AoC 19.12.2") {

        it("works with test data") {
            assertEquals(2772, Day12.solvePt2(listOf(
                "<x=-1, y=0, z=2>",
                "<x=2, y=-10, z=-7>",
                "<x=4, y=-8, z=8>",
                "<x=3, y=5, z=-1>")))
        }

        it("works with task data") {
            assertEquals(402951477454512, Day12.solvePt2(readLines("12.txt")))
        }

    }
})