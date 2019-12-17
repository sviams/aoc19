import kotlin.math.abs

data class Pos(val x: Int, val y: Int) {
    fun plus(other: Pos) = Pos(x + other.x, y + other.y)
    fun minus(other: Pos) = Pos(x - other.x, y - other.y)
    fun times(other: Pos) = Pos(x * other.x, y * other.y)
    fun distanceTo(other: Pos) = abs(x - other.x) + abs(y - other.y)
    fun product() = x * y
    override fun equals(other: Any?): Boolean {
        val o = other as Pos
        return x == o.x && y == o.y
    }
}

typealias Path = List<Pos>

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b
fun lcm(a: Long, b: Long, c: Long) = lcm(a, lcm(b,c))