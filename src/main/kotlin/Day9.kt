object Day9 {

    fun solveImmutable(program: List<Long>, input: List<Long>) : Long {
        val startState = program.foldIndexed(emptyMap<Long, Long>()) { index, acc, i ->  acc.plus(index.toLong() to i)}
        val startCpu = IntCodeComputer.immutable(startState, input)
        val endCpu = ImmutableIntCodeComputer.run(startCpu)
        return endCpu.output.last()
    }

    fun solveMutable(program: List<Long>, input: List<Long>) : Long {
        val startState = program.foldIndexed(emptyMap<Long, Long>()) { index, acc, i ->  acc.plus(index.toLong() to i)}.toMutableMap()
        return IntCodeComputer.mutable(startState, input.toMutableList()).run(0).output.last()
    }

}