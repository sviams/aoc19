import kotlin.math.roundToInt

object Day1 {

    fun fuelRequired(mass: Int): Int = (mass / 3).toDouble().roundToInt() -2

    tailrec fun fuelRecursive(mass: Int, acc: Int) : Int =
        if (mass < 0) acc else fuelRecursive(fuelRequired(mass), acc + mass)

    fun solvePt1(input: List<Int>) : Int = input.sumBy { fuelRequired(it) }

    fun solvePt2(input: List<Int>) : Int = input.sumBy { fuelRecursive(fuelRequired(it), 0) }

}