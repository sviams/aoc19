object Day21 {

    fun toAsciiInput(s: String) : List<Long> = s.toCharArray().map { it.toLong() } + 10L
    fun compile(c: List<String>) : List<Long> = c.fold(emptyList()) {acc, line -> acc + toAsciiInput(line)}

    fun solvePt1(program: ImmutableIntCodeProgram) : Long =
        ICC.run(IntCodeComputer.immutable(program, compile(listOf(
            "NOT A J",
            "NOT C T",
            "AND D T",
            "OR T J",
            "WALK"
        )))).output.last()

    fun solvePt2(program: ImmutableIntCodeProgram) : Long =
        ICC.run(IntCodeComputer.immutable(program, compile(listOf(
            "NOT A J",
            "NOT C T",
            "AND H T",
            "OR T J",
            "NOT B T",
            "AND A T",
            "AND C T",
            "OR T J",
            "AND D J",
            "RUN"
        )))).output.last()


}