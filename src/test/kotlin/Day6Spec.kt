
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day6Spec: Spek({

    given("AoC 19.6.1") {

        it("works with test data") {
            assertEquals(42, Day6.solvePt1(listOf(
                    "E)J",
                    "B)C",
                    "E)F",
                    "C)D",
                    "COM)B",
                    "K)L",
                    "D)E",
                    "B)G",
                    "G)H",
                    "D)I",
                    "J)K")))
        }

        it("works with task data") {
            assertEquals(160040, Day6.solvePt1(readLines("6.txt")))
        }
    }

    given("AoC 19.6.2") {

        it("works with test data") {
            assertEquals(4, Day6.solvePt2(listOf("COM)B",
                    "B)C",
                    "C)D",
                    "D)E",
                    "E)F",
                    "B)G",
                    "G)H",
                    "D)I",
                    "E)J",
                    "J)K",
                    "K)L",
                    "K)YOU",
                    "I)SAN")))
        }

        it("works with task data") {
            assertEquals(373, Day6.solvePt2(readLines("6.txt")))
        }

    }
})