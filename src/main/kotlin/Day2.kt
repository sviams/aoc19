import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object Day2 {

    fun parseInput(input: String): MutableList<Int> = input.split(",").map { it.toInt() }.toMutableList()

    tailrec fun doPt1(state: MutableList<Int>, pos: Int): MutableList<Int> {
        val op = state[pos]
        if (op == 99) return state
        val left = state[state[pos+1]]
        val right = state[state[pos+2]]
        val dest = state[pos+3]
        val result = if (op == 1) left + right else left * right
        state[dest] = result
        return doPt1(state, pos+4)
    }

    fun solvePt1(state: MutableList<Int>, noun: Int, verb: Int) : MutableList<Int> {
        state[1] = noun
        state[2] = verb
        return doPt1(state, 0)
    }

    tailrec fun doPt2(startState: ImmutableList<Int>, noun: Int, verb: Int): Int {
        val result = solvePt1(startState.toMutableList(), noun, verb)
        if (result.first() == 19690720) return 100*noun + verb
        val nextVerb = (verb+1) % 100
        val nextNoun = if (nextVerb == 0)  noun + 1 else noun
        return doPt2(startState, nextNoun, nextVerb)
    }

    fun solvePt2(input: String) : Int = doPt2(parseInput(input).toImmutableList(), 0,0)

}