object Day9 {

    object IntCodeComputer {
        fun mode(chars: List<Char>, index: Int): Char = chars.drop(index).firstOrNull() ?: '0'
        fun twoParams(modeChars: List<Char>, pos: Long, state: Map<Long, Long>, relBase: Long): List<Long> =
            listOf(valueOf(state, mode(modeChars, 0), state[pos+1]!!, relBase), valueOf(state, mode(modeChars, 1), state[pos+2]!!, relBase))
        fun valueOf(state: Map<Long, Long>, modeChar: Char, dest: Long, relBase: Long): Long = when (modeChar) {
            '2' -> state[dest + relBase] ?: 0
            '1' -> dest
            else -> state[dest] ?: 0
        }
        fun mutable(program: Map<Long, Long>, input: List<Long>) =
            MutableIntCodeComputer(program.toMutableMap(), emptyList<Long>().toMutableList(), input.toMutableList(), 0L, 0L, 0L, 0L)

        fun immutable(program: Map<Long, Long>, input: List<Long>) =
            ImmutableIntCodeComputer(program, emptyList(), input, 0L, 0L, 0L)
    }

    data class MutableIntCodeComputer(val state: MutableMap<Long, Long>, val output: MutableList<Long>, val input: MutableList<Long>, var lastOp: Long, var lastPos: Long, val lastOut: Long, var relBase: Long) {

        tailrec fun run(pos: Long): MutableIntCodeComputer {
            val op = state[pos]!! % 100
            if (op == 99L) {
                lastOp = 99
                return this
            }

            val modeChars = state[pos].toString().toCharArray().dropLast(2).reversed()

            val params = when (op) {
                3L,4L,9L -> emptyList()
                else -> IntCodeComputer.twoParams(modeChars, pos, state, relBase)
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
                4L -> output.add(IntCodeComputer.valueOf(state, destModeChar, lastParam, relBase))
                7L -> if (params[0] < params[1]) state[dest] = 1 else state[dest] = 0
                8L -> if (params[0] == params[1]) state[dest] = 1 else state[dest] = 0
            }

            if (op == 9L) relBase += IntCodeComputer.valueOf(state, destModeChar, lastParam, relBase)

            val nextPos = when (op) {
                3L,4L,9L -> pos+2
                5L -> if (params[0] != 0L) params[1] else pos+3
                6L -> if (params[0] == 0L) params[1] else pos+3
                else -> pos+4
            }

            return run(nextPos)
        }
    }

    data class ImmutableIntCodeComputer(val state: Map<Long, Long>, val output: List<Long>, val input: List<Long>, val lastOp: Long, val relBase: Long, val pos: Long) {

        companion object {
            tailrec fun run(cpu: ImmutableIntCodeComputer): ImmutableIntCodeComputer {
                val next = cpu.execute()
                return if (next.lastOp == 99L) next else run(next)
            }
        }

        fun execute(): ImmutableIntCodeComputer {
            val op = state[pos]!! % 100
            if (op == 99L) return ImmutableIntCodeComputer(state, output, input, op, relBase, pos)

            val modeChars = state[pos].toString().toCharArray().dropLast(2).reversed()

            val params = when (op) {
                3L,4L,9L -> emptyList()
                else -> IntCodeComputer.twoParams(modeChars, pos, state, relBase)
            }

            val destModeChar = if (modeChars.drop(params.size).isNotEmpty()) modeChars.last() else '0'
            val lastParam = state[pos+1+params.size] ?: 0L
            val dest: Long = when (destModeChar) {
                '2' -> lastParam + relBase
                else -> lastParam
            }

            val nextLastOp = op

            if (op == 3L && input.isEmpty()) return this


            val nextState: Map<Long, Long> = when (op) {
                1L -> state + (dest to params[0] + params[1])
                2L -> state + (dest to params[0] * params[1])
                3L -> state + (dest to input[0])
                7L -> if (params[0] < params[1]) state + (dest to 1L) else state + (dest to 0L)
                8L -> if (params[0] == params[1]) state + (dest to 1L) else state + (dest to 0L)
                else -> state
            }

            val nextInput = if (op == 3L) input.drop(1) else input
            val nextOutput = if (op == 4L) output + IntCodeComputer.valueOf(state, destModeChar, lastParam, relBase) else output
            val nextRelBase = if (op == 9L) relBase + IntCodeComputer.valueOf(state, destModeChar, lastParam, relBase) else relBase

            val nextPos = when (op) {
                3L,4L,9L -> pos+2
                5L -> if (params[0] != 0L) params[1] else pos+3
                6L -> if (params[0] == 0L) params[1] else pos+3
                else -> pos+4
            }
            return ImmutableIntCodeComputer(nextState, nextOutput, nextInput, nextLastOp, nextRelBase, nextPos)
        }
    }

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