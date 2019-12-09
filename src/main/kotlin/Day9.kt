object Day9 {

    data class IntCodeComputer(val state: MutableMap<Long, Long>, val output: MutableList<Long>, val input: MutableList<Long>, var lastOp: Long, var lastPos: Long, val lastOut: Long, var relBase: Long) {

        fun mode(chars: List<Char>, index: Int): Char = chars.drop(index).firstOrNull() ?: '0'

        fun twoParams(modeChars: List<Char>, pos: Long): List<Long> =
            listOf(valueOf(mode(modeChars, 0), state[pos+1]!!), valueOf(mode(modeChars, 1), state[pos+2]!!))

        fun valueOf(modeChar: Char, dest: Long): Long = when (modeChar) {
            '2' -> state[dest + relBase] ?: 0
            '1' -> dest
            else -> state[dest] ?: 0
        }

        tailrec fun run(pos: Long): IntCodeComputer {
            val asd = state[pos]!!
            val op = asd % 100
            if (op == 99L) {
                lastOp = 99
                return this
            }

            val modeChars = state[pos].toString().toCharArray().dropLast(2).reversed()

            val params = when (op) {
                3L,4L,9L -> emptyList()
                else -> twoParams(modeChars, pos)
            }

            val destModeChar = if (modeChars.drop(params.size).isNotEmpty()) modeChars.last() else '0'
            val lastParam = state[pos+1+params.size] ?: 0L
            val dest: Long = when (destModeChar) {
                '2' -> lastParam + relBase
                else -> lastParam
            }

            lastPos = pos
            lastOp = op
            when (op) {
                1L -> state[dest] = params[0] + params[1]
                2L -> state[dest] = params[0] * params[1]
                3L -> state[dest] = if (input.size > 0 ) input.removeAt(0) else return this
                4L -> output.add(valueOf(destModeChar, lastParam))
                7L -> if (params[0] < params[1]) state[dest] = 1 else state[dest] = 0
                8L -> if (params[0] == params[1]) state[dest] = 1 else state[dest] = 0
            }

            if (op == 9L) relBase += valueOf(destModeChar, lastParam)

            val nextPos = when (op) {
                3L,4L,9L -> pos+2
                5L -> if (params[0] != 0L) params[1] else pos+3
                6L -> if (params[0] == 0L) params[1] else pos+3
                else -> pos+4
            }

            return run(nextPos)
        }


    }

    fun solve(program: List<Long>, input: List<Long>) : Long {
        val startState = program.foldIndexed(emptyMap<Long, Long>()) { index, acc, i ->  acc.plus(index.toLong() to i)}.toMutableMap()
        return IntCodeComputer(startState, emptyList<Long>().toMutableList(), input.toMutableList(), 0L, 0L, 0L, 0L).run(0).output.last()
    }

}