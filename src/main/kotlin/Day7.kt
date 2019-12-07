import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object Day7 {

    fun get(mode: Char, param: Int, state: MutableList<Int>) = if (mode == '0') state[param] else param

    fun mode(chars: List<Char>, index: Int): Char = chars.drop(index).firstOrNull() ?: '0'

    fun twoParams(state: MutableList<Int>, modeChars: List<Char>, pos: Int) =
        listOf(get(mode(modeChars, 0), state[pos+1], state), get(mode(modeChars, 1), state[pos+2], state))

    data class Amp(val state: MutableList<Int>, val output: MutableList<Int>, val input: MutableList<Int>, var lastOp: Int, var lastPos: Int, val lastOut: Int) {
        tailrec fun runIntCode(pos: Int): Amp {
            val op = state[pos] % 100
            if (op == 99) {
                lastOp = 99
                return this
            }

            val modeChars = state[pos].toString().toCharArray().dropLast(2).reversed()

            val params = when (op) {
                3,4 -> emptyList()
                else -> twoParams(state, modeChars, pos)
            }
            val dest = state[pos+1+params.size]
            lastPos = pos
            lastOp = op
            when (op) {
                1 -> state[dest] = params[0] + params[1]
                2 -> state[dest] = params[0] * params[1]
                3 -> state[dest] = if (input.size > 0 ) input.removeAt(0) else return this
                4 -> output.add(state[dest])
                7 -> if (params[0] < params[1]) state[dest] = 1 else state[dest] = 0
                8 -> if (params[0] == params[1]) state[dest] = 1 else state[dest] = 0
            }

            val nextPos = when (op) {
                3,4 -> pos+2
                5 -> if (params[0] != 0) params[1] else pos+3
                6 -> if (params[0] == 0) params[1] else pos+3
                else -> pos+4
            }
            return runIntCode(nextPos)
        }
    }

    tailrec fun runUntilHalt(amps: List<Amp>, index: Int, original: List<Int>) : Int {
        val prevIndex = if (index == 0) 4 else index - 1
        val prevAmp = amps[prevIndex]
        if (amps.all { it.lastOp == 99 }) return prevAmp.output.last()
        val amp = amps[index]
        if (prevAmp.output.isNotEmpty()) amp.input.add(prevAmp.output.last())
        else if (index == 0 && amp.state == original) amp.input.add(0)
        if (amp.input.isNotEmpty() && amp.lastOp != 99) amp.runIntCode(amp.lastPos)
        val nextIndex = (index+1) % 5
        return runUntilHalt(amps, nextIndex, original)
    }

    tailrec fun findMaxOutput(program: ImmutableList<Int>, ampSettings: List<Int>, maxOutput: Int, settingOffset: Int, ampStates: List<MutableList<Int>>): Int {
        if (ampSettings == listOf(0,1,2,3,4).map { it + settingOffset } && maxOutput > 0) return maxOutput
        val amps = (0 until 5).map { Amp(program.toMutableList(), mutableListOf(), mutableListOf(ampSettings[it]), 0, 0, 0) }
        val output = runUntilHalt(amps, 0, program)
        val nextMax = if (output > maxOutput) output else maxOutput
        val nextSettings = nextSettings(ampSettings, settingOffset)
        return findMaxOutput(program, nextSettings, nextMax, settingOffset, ampStates)
    }

    tailrec fun nextSettings(last: List<Int>, settingOffset: Int) : List<Int> {
        val lastSettings = last.map { it - settingOffset }.joinToString("").toInt(5)
        val lastChars = lastSettings.plus(1).toString(5).toCharArray()
        val nextSettings = lastChars.map { it.toInt() -48 }
        val f = (listOf(0,0,0,0,0) + nextSettings).takeLast(5).map { it + settingOffset }
        return if (f.distinct().size == f.size) f
        else nextSettings(f, settingOffset)
    }

    fun solvePt1(input: List<Int>) : Int {
        return findMaxOutput(input.toImmutableList(), listOf(0,1,2,3,4), 0, 0, (0 .. 5).map { input.toMutableList() })
    }

    fun solvePt2(input: List<Int>) : Int {
        return findMaxOutput(input.toImmutableList(), listOf(5,6,7,8,9), 0, 5, (0 .. 5).map { input.toMutableList() })
    }

}