import kotlin.math.abs

object Day16 {

    fun multiplier(outputIndex: Int, patternIndex: Int) : Int {
        val x = ((patternIndex+1) % (4 * (outputIndex)))
        return when {
            x < outputIndex -> 0
            x < outputIndex * 2 -> 1
            x < outputIndex * 3 -> 0
            else -> -1
        }
    }

    tailrec fun sumByIndex(input: List<Int>, total: Int, index: Int, outputIndex: Int) : Int =
        if (index == input.size) total
        else sumByIndex(input, total + input[index] * multiplier(outputIndex+1, index), index + 1, outputIndex)

    fun calculate(input: List<Int>, index: Int) = abs(sumByIndex(input, 0, 0, index)) % 10

    tailrec fun phase(input: List<Int>, count: Int, limit: Int) : List<Int> =
        if (count == limit) input.take(8).toList()
        else phase(input.mapIndexed { index, i -> calculate(input, index) }, count + 1, limit)

    tailrec fun phaseReversed(input: MutableList<Int>, count: Int, limit: Int) : List<Int> {
        if (count == limit) return input.take(8).toList()
        (input.size-1 downTo  0).forEach { i -> input[i] = (input[i] + (input.getOrNull(i+1) ?: 0)) % 10 }
        return phaseReversed(input, count + 1, limit)
    }

    fun solvePt1(input: List<Int>) : Int = phase(input, 0, 100).joinToString("").toInt()

    fun solvePt2(input: List<Int>) : Int {
        val offset = input.take(7).joinToString("").toInt()
        val bigInput = generateSequence { input }.take(10000).flatten().drop(offset).toMutableList()
        return phaseReversed(bigInput, 0, 100).joinToString("").toInt()
    }
}
