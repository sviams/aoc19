
object Day5 {

    fun get(mode: Char, param: Int, state: MutableList<Int>) = if (mode == '0') state[param] else param

    fun mode(chars: List<Char>, index: Int): Char = chars.drop(index).firstOrNull() ?: '0'

    fun twoParams(state: MutableList<Int>, modeChars: List<Char>, pos: Int) =
        listOf(get(mode(modeChars, 0), state[pos+1], state), get(mode(modeChars, 1), state[pos+2], state))

    tailrec fun runIntCode(state: MutableList<Int>, pos: Int, theInput: Int, output: MutableList<Int>): MutableList<Int> {
        val op = state[pos] % 100
        if (op == 99) return output
        val modeChars = state[pos].toString().toCharArray().dropLast(2).reversed()

        val params = when (op) {
            3,4 -> emptyList()
            else -> twoParams(state, modeChars, pos)
        }
        val dest = state[pos+1+params.size]
        when (op) {
            1 -> state[dest] = params[0] + params[1]
            2 -> state[dest] = params[0] * params[1]
            3 -> state[dest] = theInput
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
        return runIntCode(state, nextPos, theInput, output)
    }

    fun solvePt1(input: List<Int>) : Int = runIntCode(input.toMutableList(), 0, 1, mutableListOf()).last()

    fun solvePt2(input: List<Int>) : Int = runIntCode(input.toMutableList(), 0, 5, mutableListOf()).last()

}