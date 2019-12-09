
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day9Spec: Spek({

    given("AoC 19.9.1") {

        it("works with test data") {
            assertEquals(99, Day9.solveMutable(listOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99), listOf(1L)))
        }

        it("works with test data 2") {
            assertEquals(1219070632396864, Day9.solveMutable(listOf(1102,34915192,34915192,7,4,7,99,0), listOf(1L)))
        }

        it("works with test data 3") {
            assertEquals(1125899906842624L, Day9.solveMutable(listOf(104,1125899906842624,99), listOf(1L)))
        }

        it("works with Day 5 task data") {
            assertEquals(6745903, Day9.solveMutable(readLines("5.txt").first().split(",").map { it.toLong() }, listOf(1L)))
        }

        it("works with task data mutable") {
            assertEquals(2465411646, Day9.solveMutable(readLines("9.txt").first().split(",").map { it.toLong() }, listOf(1L)))
        }

        it("works with task data immutable") {
            assertEquals(2465411646, Day9.solveImmutable(readLines("9.txt").first().split(",").map { it.toLong() }, listOf(1L)))
        }
    }

    given("AoC 19.9.2") {

        it("works with task data mutable") {
            assertEquals(69781, Day9.solveMutable(readLines("9.txt").first().split(",").map { it.toLong() }, listOf(2L)))
        }

        it("works with task data immutable") {
            assertEquals(69781, Day9.solveImmutable(readLines("9.txt").first().split(",").map { it.toLong() }, listOf(2L)))
        }

    }
})