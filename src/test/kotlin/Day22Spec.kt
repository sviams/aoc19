
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object Day22Spec: Spek({

    given("AoC 19.22.1") {

        it("works with test data") {
            assertEquals(4126, Day22.solvePt1(listOf("deal with increment 7",
                    "deal into new stack",
                    "deal into new stack"), 10007, 2019))
        }

        it("works with many cuts and increments") {
            assertEquals(1219, Day22.solvePt1(listOf("deal into new stack",
                    "cut -2",
                    "deal with increment 7",
                    "cut 8",
                    "cut -4",
                    "deal with increment 7",
                    "cut 3",
                    "deal with increment 9",
                    "deal with increment 3",
                    "cut -1"), 10007, 2019))
        }

        it("works with increment") {
            assertEquals(4302, Day22.solvePt1(listOf("deal with increment 17"), 10007, 2019))
        }

        it("works with test data 4") {
            assertEquals(2016, Day22.solvePt1(listOf("cut 3"), 10007, 2019))
        }

        it("works with test data 5") {
            assertEquals(2023, Day22.solvePt1(listOf("cut -4"), 10007, 2019))
        }

        it("works with test data 6") {
            assertEquals(7987, Day22.solvePt1(listOf("deal into new stack"), 10007, 2019))
        }

        it("works with test data 7") {
            assertEquals(5922, Day22.solvePt1(listOf("cut 6",
                "deal with increment 7",
                "deal into new stack"), 10007, 2019))
        }

        it("works with cut and reverse") {
            assertEquals(7992, Day22.solvePt1(listOf("cut 5", "deal into new stack"), 10007, 2019))
        }

        it("works with reverse and cut") {
            assertEquals(7982, Day22.solvePt1(listOf("deal into new stack", "cut 5"), 10007, 2019))
        }

        it("works with task data") {
            assertEquals(6696, Day22.solvePt1(readLines("22.txt"), 10007, 2019))
        }
    }

    given("AoC 19.22.2") {

        it("works with test data") {
            assertEquals(1718.toBigInteger(), Day22.solvePt2(listOf("deal with increment 7",
                "deal into new stack",
                "deal into new stack"), 10007.toBigInteger(), 2019.toBigInteger(), 1.toBigInteger()))
        }

        it("works with many cuts and increments") {
            assertEquals(5702.toBigInteger(), Day22.solvePt2(listOf("deal into new stack",
                "cut -2",
                "deal with increment 7",
                "cut 8",
                "cut -4",
                "deal with increment 7",
                "cut 3",
                "deal with increment 9",
                "deal with increment 3",
                "cut -1"), 10007.toBigInteger(), 2019.toBigInteger(), 1.toBigInteger()))
        }

        it("works with many cuts and increments 29 times") {
            assertEquals(3003.toBigInteger(), Day22.solvePt2(listOf("deal into new stack",
                "cut -2",
                "deal with increment 7",
                "cut 8",
                "cut -4",
                "deal with increment 7",
                "cut 3",
                "deal with increment 9",
                "deal with increment 3",
                "cut -1"), 10007.toBigInteger(), 2019.toBigInteger(), 29.toBigInteger()))
        }

        it("works with many cuts and increments 104729 times") {
            assertEquals(1726.toBigInteger(), Day22.solvePt2(listOf("deal into new stack",
                "cut -2",
                "deal with increment 7",
                "cut 8",
                "cut -4",
                "deal with increment 7",
                "cut 3",
                "deal with increment 9",
                "deal with increment 3",
                "cut -1"), 10007.toBigInteger(), 2019.toBigInteger(), 104729.toBigInteger()))
        }

        it("works with many cuts and increments 29 times with 104729 cards") {
            assertEquals(77677.toBigInteger(), Day22.solvePt2(listOf("deal into new stack",
                "cut -2",
                "deal with increment 7",
                "cut 8",
                "cut -4",
                "deal with increment 7",
                "cut 3",
                "deal with increment 9",
                "deal with increment 3",
                "cut -1"), 104729.toBigInteger(), 2019.toBigInteger(), 29.toBigInteger()))
        }

        it("works with many cuts and increments 29 times with 1000003 cards") {
            assertEquals(119185.toBigInteger(), Day22.solvePt2(listOf("deal into new stack",
                "cut -2",
                "deal with increment 7",
                "cut 8",
                "cut -4",
                "deal with increment 7",
                "cut 3",
                "deal with increment 9",
                "deal with increment 3",
                "cut -1"), 1000003.toBigInteger(), 2019.toBigInteger(), 29.toBigInteger()))
        }

        it("works with increment") {
            assertEquals(3062.toBigInteger(), Day22.solvePt2(listOf("deal with increment 17"), 10007.toBigInteger(), 2019.toBigInteger(), 1.toBigInteger()))
        }

        it("works with increment twice") {
            assertEquals(3712.toBigInteger(), Day22.solvePt2(listOf("deal with increment 17"), 10007.toBigInteger(), 2019.toBigInteger(), 2.toBigInteger()))
        }

        it("works with reverse") {
            assertEquals(2019.toBigInteger(), Day22.solvePt2(listOf("deal into new stack"), 10007.toBigInteger(), 2019.toBigInteger(), 2.toBigInteger()))
        }

        it("works with reverse twice") {
            assertEquals(7987.toBigInteger(), Day22.solvePt2(listOf("deal into new stack"), 10007.toBigInteger(), 2019.toBigInteger(), 1.toBigInteger()))
        }

        it("works with all three instructions") {
            assertEquals(1147.toBigInteger(), Day22.solvePt2(listOf("cut 6",
                "deal with increment 7",
                "deal into new stack"), 10007.toBigInteger(), 2019.toBigInteger(), 1.toBigInteger()))
        }

        it("works with all three instructions twice") {
            assertEquals(9849.toBigInteger(), Day22.solvePt2(listOf("cut 6",
                "deal with increment 7",
                "deal into new stack"), 10007.toBigInteger(), 2019.toBigInteger(), 2.toBigInteger()))
        }

        it("works with cut and reverse") {
            assertEquals(7992.toBigInteger(), Day22.solvePt2(listOf("cut 5", "deal into new stack"), 10007.toBigInteger(), 2019.toBigInteger(), 1.toBigInteger()))
        }

        it("works with reverse and cut") {
            assertEquals(7982.toBigInteger(), Day22.solvePt2(listOf("deal into new stack", "cut 5"), 10007.toBigInteger(), 2019.toBigInteger(), 1.toBigInteger()))
        }

        it("works with task data for pt1 for 1 iteration") {
            assertEquals(253.toBigInteger(), Day22.solvePt2(readLines("22.txt"), 10007.toBigInteger(), 2019.toBigInteger(), 1.toBigInteger()))
        }

        it("works with task data for pt1 for 100 iteration") {
            assertEquals(6008.toBigInteger(), Day22.solvePt2(readLines("22.txt"), 10007.toBigInteger(), 2019.toBigInteger(), 103.toBigInteger()))
        }

        it("works with task data") {
            assertEquals(93750418158025.toBigInteger(), Day22.solvePt2(readLines("22.txt"), 119315717514047.toBigInteger(), 2020.toBigInteger(), 101741582076661L.toBigInteger()))
        }

    }
})