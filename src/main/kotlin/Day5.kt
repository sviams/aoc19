
object Day5 {

    fun solvePt1(input: List<Long>) : Int =
        ICC.run(IntCodeComputer.immutableFrom(input, listOf(1))).output.last().toInt()


    fun solvePt2(input: List<Long>) : Int =
        ICC.run(IntCodeComputer.immutableFrom(input, listOf(5))).output.last().toInt()
}