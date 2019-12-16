
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day16Spec: Spek({

    given("AoC 19.16.1") {

        it("works with test data") {
            assertEquals(23845678, Day16.solvePt1("12345678".toCharArray().map { it.toInt() - 48 }))
        }

        it("works with test data 2") {
            assertEquals(24176176, Day16.solvePt1("80871224585914546619083218645595".toCharArray().map { it.toInt() - 48 }))
        }

        it("works with test data 3") {
            assertEquals(73745418, Day16.solvePt1("19617804207202209144916044189917".toCharArray().map { it.toInt() - 48 }))
        }

        it("works with test data 4") {
            assertEquals(52432133, Day16.solvePt1("69317163492948606335995924319873".toCharArray().map { it.toInt() - 48 }))
        }

        it("works with task data immutable") {
            assertEquals(78009100, Day16.solvePt1(readLines("16.txt").first().toCharArray().map { it.toInt() - 48 }))
        }
    }

    given("AoC 19.16.2") {

        it("works with test data 2") {
            assertEquals(84462026, Day16.solvePt2("03036732577212944063491565474664".toCharArray().map { it.toInt() - 48 }))
        }

        it("works with test data 3") {
            assertEquals(78725270, Day16.solvePt2("02935109699940807407585447034323".toCharArray().map { it.toInt() - 48 }))
        }

        it("works with test data 4") {
            assertEquals(53553731, Day16.solvePt2("03081770884921959731165446850517".toCharArray().map { it.toInt() - 48 }))
        }

        it("works with task data immutable") {
            assertEquals(37717791, Day16.solvePt2(readLines("16.txt").first().toCharArray().map { it.toInt() - 48 }))
        }

    }
})