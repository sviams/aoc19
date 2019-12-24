import java.math.BigInteger
import kotlin.math.abs

object Day22 {

    fun cut(deck: List<Int>, move: String) : List<Int> {
        val amount = move.substring(4).toInt()
        return when {
            amount >= 0 -> deck.takeLast(deck.size - amount) + deck.take(amount)
            else -> deck.takeLast(abs(amount)) + deck.take(deck.size - abs(amount))
        }
    }

    fun dealWithIncrement(deck: List<Int>, move: String) : List<Int> {
        val increment = move.substring(20).toInt()
        val target = MutableList<Int>(deck.size) { i -> 0}
        deck.forEachIndexed { index, i -> target[(index * increment) % deck.size] = i}
        return target.toList()
    }

    fun performMove(deck: List<Int>, move: String): List<Int> =
        when {
            move.startsWith("cut") -> cut(deck, move)
            move.startsWith("deal with") -> dealWithIncrement(deck, move)
            else -> deck.reversed()
        }


    fun solvePt1(input: List<String>, deckSize: Int, target: Int) : Int {
        val deck: List<Int> = (0 until deckSize).toList()
        val result = input.fold(deck) { deck, move -> performMove(deck, move) }
        return result.indexOf(target)
    }

    fun reverse(deckSize: BigInteger): (Pair<BigInteger, BigInteger>) -> Pair<BigInteger, BigInteger> =
        { (offset, increment) ->
            val newInc = (increment * (-1).toBigInteger())
            (offset + newInc) % deckSize to newInc
        }

    fun cut(line: String, deckSize: BigInteger): (Pair<BigInteger, BigInteger>) -> Pair<BigInteger, BigInteger> =
        { (offset, increment) -> (offset + line.substring(4).toBigInteger() * increment) % deckSize to increment}

    fun deal(line: String, deckSize: BigInteger): (Pair<BigInteger, BigInteger>) -> Pair<BigInteger, BigInteger> {
        val modInverse = line.substring(20).toBigInteger().modPow(deckSize.minus(BigInteger.valueOf(2)), deckSize)
        return { (offset, increment) -> offset to (increment * modInverse) % deckSize}
    }

    fun translateMove(move: String, deckSize: BigInteger): (Pair<BigInteger, BigInteger>) -> Pair<BigInteger, BigInteger> =
        when {
            move.startsWith("cut") -> cut(move, deckSize)
            move.startsWith("deal with") -> deal(move, deckSize)
            else -> reverse(deckSize)
        }

    fun solvePt2(input: List<String>, deckSize: BigInteger, target: BigInteger, iterations: BigInteger) : BigInteger {
        val reverseInput = input.map { line -> translateMove(line, deckSize) }
        val (offset, increment) = reverseInput.fold((BigInteger.ZERO to BigInteger.ONE)) { acc, op -> op(acc) }
        val invIncrement = BigInteger.ONE.minus(increment).modPow(deckSize.minus(BigInteger.valueOf(2)), deckSize)

        val finalIncrement = increment.modPow(iterations, deckSize)
        val finalOffset = offset * (BigInteger.ONE - finalIncrement) * invIncrement
        return (finalOffset + (target * finalIncrement)).mod(deckSize)
    }
}