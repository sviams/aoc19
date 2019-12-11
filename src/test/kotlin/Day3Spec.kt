
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day3Spec: Spek({

    given("AoC 19.3.1") {

        it("works with test data") {
            assertEquals(6, Day3.solvePt1(listOf("R8,U5,L5,D3", "U7,R6,D4,L4")))
        }

        it("works with test data 2") {
            assertEquals(159, Day3.solvePt1(listOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")))
        }

        it("works with test data 3") {
            assertEquals(135, Day3.solvePt1(listOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")))
        }

        it("works with task data") {
            assertEquals(280, Day3.solvePt1(readLines("3.txt")))
        }
    }

    given("AoC 19.3.2") {

        it("works with test data") {
            assertEquals(30, Day3.solvePt2(listOf("R8,U5,L5,D3", "U7,R6,D4,L4")))
        }

        it("works with test data 2") {
            assertEquals(610, Day3.solvePt2(listOf("R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83")))
        }

        it("works with test data 3") {
            assertEquals(410, Day3.solvePt2(listOf("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")))
        }

        it("works with task data") {
            assertEquals(10554, Day3.solvePt2(readLines("3.txt")))
        }

    }
})