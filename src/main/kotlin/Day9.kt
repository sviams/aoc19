object Day9 {

    fun solveImmutable(program: List<Long>, input: List<Long>) : Long =
        ICC.run(IntCodeComputer.immutableFrom(program, input)).output.last()

    fun solveMutable(program: List<Long>, input: List<Long>) : Long =
        IntCodeComputer.mutable(IntCodeComputer.parseMutable(program), input).run(0).output.last()
}