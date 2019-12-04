
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object Day4Spec: Spek({

    given("AoC 19.4.1") {

        it("works with test data") {
            assertEquals(2, Day4.solvePt1("111111-111112"))
        }

        it("works with test data 2") {
            assertEquals(0, Day4.solvePt1("123456-123456"))
        }

        it("works with test data 3") {
            assertEquals(0, Day4.solvePt1("223450-223451"))
        }

        it("works with test data 4") {
            assertEquals(0, Day4.solvePt1("345022-345022"))
        }

        it("works with task data") {
            assertEquals(1330, Day4.solvePt1("231832-767346"))
        }
    }

    given("AoC 19.4.2") {

        it("works with test data") {
            assertEquals(1, Day4.solvePt2("112233-112233"))
        }

        it("works with test data 2") {
            assertEquals(0, Day4.solvePt2("123444-123444"))
        }

        it("works with test data 3") {
            assertEquals(1, Day4.solvePt2("111122-111122"))
        }

        it("works with test data 4") {
            assertEquals(1, Day4.solvePt2("112222-112222"))
        }

        it("works with test data 5") {
            assertEquals(0, Day4.solvePt2("111123-111123"))
        }

        it("works with task data") {
            assertEquals(876, Day4.solvePt2("231832-767346"))
        }

    }
})