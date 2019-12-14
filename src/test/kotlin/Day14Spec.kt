
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day14Spec: Spek({

    given("AoC 19.14.1") {

        it("works with test data") {
            assertEquals(31, Day14.solvePt1(listOf("10 ORE => 10 A",
                    "1 ORE => 1 B",
                    "7 A, 1 B => 1 C",
                    "7 A, 1 C => 1 D",
                    "7 A, 1 D => 1 E",
                    "7 A, 1 E => 1 FUEL")))
        }

        it("works with test data 2") {
            assertEquals(165, Day14.solvePt1(listOf("9 ORE => 2 A",
                    "8 ORE => 3 B",
                    "7 ORE => 5 C",
                    "3 A, 4 B => 1 AB",
                    "5 B, 7 C => 1 BC",
                    "4 C, 1 A => 1 CA",
                    "2 AB, 3 BC, 4 CA => 1 FUEL")))
        }

        it("works with task data") {
            assertEquals(843220, Day14.solvePt1(readLines("14.txt")))
        }
    }

    given("AoC 19.14.2") {

        it("works with task data") {
            assertEquals(2169535, Day14.solvePt2(readLines("14.txt")))
        }

    }
})