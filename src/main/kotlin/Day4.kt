import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object Day4 {

    fun hasDuplicate(chars: CharArray) : Boolean = chars.fold(Pair(-1, false)) {acc, c ->
        if (acc.first == c.toInt()) Pair(c.toInt(), true) else Pair(c.toInt(), acc.second)
    }.second

    fun hasNoDescending(chars: CharArray) : Boolean = chars.fold(Pair(-1, true)) {acc, c ->
        if (acc.first > c.toInt() ) Pair(c.toInt(), false) else Pair(c.toInt(), acc.second)
    }.second

    fun hasSingleDuplicate(chars: CharArray) : Boolean = chars.foldIndexed(false) {i, acc, c ->
        if (i == 2 && chars[i-2] == chars[i-1] && c != chars[i-1]) true
        else if (i > 2 && chars[i-3] != chars[i-2] && chars[i-2] == chars[i-1] && c != chars[i-1]) true
        else if (i == chars.size - 1 && chars[i-1] == c && chars[i-2] != c) true
        else acc
    }

    fun solvePt1(input: String) : Int {
        val (start, end) = """(\d+)-(\d+)""".toRegex().find(input)!!.destructured
        return (start.toInt() .. end.toInt()).fold(0) { acc, i ->
            val chars = i.toString().toCharArray()
            if (hasDuplicate(chars) && hasNoDescending(chars)) acc + 1 else acc
        }
    }

    fun solvePt2(input: String) : Int {
        val (start, end) = """(\d+)-(\d+)""".toRegex().find(input)!!.destructured
        return (start.toInt() .. end.toInt()).fold(0) { acc, i ->
            val chars = i.toString().toCharArray()
            if (hasNoDescending(chars) && hasSingleDuplicate(chars)) acc + 1 else acc
        }
    }


}