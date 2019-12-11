
object Day5 {

    fun solvePt1(input: List<Long>) : Int =
        IntCodeComputer.mutable(IntCodeComputer.parse(input), mutableListOf(1)).run(0).output.last().toInt()


    fun solvePt2(input: List<Long>) : Int =
        IntCodeComputer.mutable(IntCodeComputer.parse(input), mutableListOf(5)).run(0).output.last().toInt()


}