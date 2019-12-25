fun main(args: Array<String>) {
    val ic = Day25::class.java.classLoader.getResource("25.txt").readText().split("\n").first().split(",").map { it.toLong() }
    Day25.runManual(IntCodeComputer.mutable(IntCodeComputer.parse(ic), emptyList()), emptyList())
}

object Day25 {

    fun toAsciiInput(s: String) : List<Long> = s.toCharArray().map { it.toLong() } + 10L
    fun compile(c: List<String>) : List<Long> = c.fold(emptyList()) {acc, line -> acc + toAsciiInput(line)}

    tailrec fun runManual(cpu: MutableIntCodeComputer, input: List<String>) : Long {
        if (cpu.lastOp == 99L) return 99L
        val after = cpu.copy(input = compile(input).toMutableList(), output = mutableListOf()).run(cpu.lastPos)
        System.out.println(after.output.map { it.toChar() }.joinToString(""))
        val nextInput = readLine()
        return runManual(after, listOf(nextInput ?: ""))
    }

    fun solvePt1(program: IntCodeProgram) : Long {
        val out = IntCodeComputer.mutable(program, compile(listOf(
            "east", "south", "west", "north", "take candy cane",
            "south", "east", "north", "east", "take fixed point",
            "north", "west", "take shell",
            "east", "take spool of cat6",
            "south", "west", "west", "north", "north", "east", "south"
            ))).run(0)
        val outString = out.output.map { it.toChar() }.joinToString("")
        val (result) = """typing (\d+) on the keypad""".toRegex().find(outString)!!.destructured
        return result.toLong()
    }
}