import kotlinx.collections.immutable.*

object IntCodeComputer {
    fun mode(chars: List<Char>, index: Int): Char = chars.drop(index).firstOrNull() ?: '0'
    fun twoParams(modeChars: List<Char>, pos: Long, state: Map<Long, Long>, relBase: Long): List<Long> =
        listOf(valueOf(state, mode(modeChars, 0), state[pos+1]!!, relBase), valueOf(state, mode(modeChars, 1), state[pos+2]!!, relBase))
    fun valueOf(state: Map<Long, Long>, modeChar: Char, dest: Long, relBase: Long): Long = when (modeChar) {
        '2' -> state[dest + relBase] ?: 0
        '1' -> dest
        else -> state[dest] ?: 0
    }
    fun mutable(program: MutableIntCodeProgram, input: List<Long>) =
        MutableIntCodeComputer(program.toMutableMap(), emptyList<Long>().toMutableList(), input.toMutableList(), 0L, 0L, 0L, 0L)

    fun immutable(program: ImmutableIntCodeProgram, input: List<Long>) =
        ICC(program.toPersistentHashMap(), persistentListOf(), input.toPersistentList(), 0L, 0L, 0L)

    fun immutableFrom(raw: List<Long>, input: List<Long>) =
        ICC(parse(raw).toPersistentHashMap(), persistentListOf(), input.toPersistentList(), 0L, 0L, 0L)

    fun parseMutable(input: List<Long>) : MutableIntCodeProgram = input.foldIndexed(mutableMapOf()) { index, acc, i ->
        acc.put(index.toLong(), i)
        acc
    }

    fun parse(input: List<Long>) : ImmutableIntCodeProgram = input.foldIndexed(persistentHashMapOf()) { index, acc, i ->  acc.put(index.toLong(), i)}

}

typealias MutableIntCodeProgram = MutableMap<Long, Long>
typealias ImmutableIntCodeProgram = PersistentMap<Long, Long>
//typealias IntCodeProgram = Map<Long, Long>

data class MutableIntCodeComputer(val state: MutableIntCodeProgram, val output: MutableList<Long>, val input: MutableList<Long>, var lastOp: Long, var lastPos: Long, val lastOut: Long, var relBase: Long) {

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

data class ICC(val state: PersistentMap<Long, Long>, val output: PersistentList<Long>, val input: PersistentList<Long>, val lastOp: Long, val relBase: Long, val pos: Long) {

    companion object {
        tailrec fun run(cpu: ICC): ICC {
            val next = cpu.execute()
            return if (next.lastOp == 99L || (cpu.pos == next.pos && next.input.isEmpty())) next else run(next)
        }
    }

    fun execute(): ICC {
        val op = state[pos]!! % 100
        if (op == 99L) return ICC(state, output, input, op, relBase, pos)

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

        if (op == 3L && input.isEmpty()) return ICC(state, output, input, op, relBase, pos)

        val nextState: PersistentMap<Long, Long> = when (op) {
            1L -> state.put(dest, params[0] + params[1])
            2L -> state.put(dest, params[0] * params[1])
            3L -> state.put(dest, input[0])
            7L -> if (params[0] < params[1]) state.put(dest, 1L) else state.put(dest, 0L)
            8L -> if (params[0] == params[1]) state.put(dest, 1L) else state.put(dest, 0L)
            else -> state
        }

        val nextInput = if (op == 3L) input.removeAt(0) else input
        val nextOutput = if (op == 4L) output.add(IntCodeComputer.valueOf(state, destModeChar, lastParam, relBase)) else output
        val nextRelBase = if (op == 9L) relBase + IntCodeComputer.valueOf(state, destModeChar, lastParam, relBase) else relBase

        val nextPos = when (op) {
            3L,4L,9L -> pos+2
            5L -> if (params[0] != 0L) params[1] else pos+3
            6L -> if (params[0] == 0L) params[1] else pos+3
            else -> pos+4
        }
        return ICC(nextState, nextOutput, nextInput, nextLastOp, nextRelBase, nextPos)
    }
}