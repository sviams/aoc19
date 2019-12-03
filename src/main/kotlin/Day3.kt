import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class Pos(val x: Int, val y: Int, val steps: Int) {
    fun move(other: Pos) = Pos(x + other.x, y + other.y, other.steps)
    fun times(other: Pos) = Pos(x * other.x, y * other.y, steps)
    fun distanceTo(other: Pos) = Math.abs(x - other.x) + Math.abs(y - other.y)
    override fun equals(other: Any?): Boolean {
        val o = other as Pos
        return x == o.x && y == o.y
    }
}

typealias Path = List<Pos>

object Day3 {

    val CENTRAL = Pos(0,0, 0)
    val DOWN = Pos(0, -1, 0)
    val UP = Pos(0, 1, 0)
    val LEFT = Pos(-1, 0, 0)
    val RIGHT = Pos(1, 0, 0)

    fun pathToPoints(input: String) : List<Pos> =
        input.split(",").fold(listOf(Pos(0,0, 0))) { acc, move ->
            val delta: Pos = when (move.first()) {
                'D' -> DOWN
                'U' -> UP
                'R' -> RIGHT
                else -> LEFT
            }
            val dist = move.substring(1).toInt()
            (1 .. dist).fold(acc) { dAcc, dDist ->
                val totalDelta = Pos(delta.x * dDist, delta.y * dDist, dAcc.last().steps + 1)
                dAcc.plus(acc.last().move(totalDelta))
            }
        }

    fun intersections(paths: List<Path>) : ImmutableList<Pos> {
        val one = paths.first()
        val other = paths.last()
        return one.fold(emptyList<Pos>()) { acc, pos ->
            if (other.contains(pos)) {
                val fromOther = other.get(other.indexOf(pos))
                (acc + Pos(pos.x, pos.y, pos.steps + fromOther.steps))
            }
            else acc
        }.toImmutableList()
    }

    fun solvePt1(input: List<String>) : Int = intersections(input.map { pathToPoints(it) }).map { it.distanceTo(CENTRAL) }.sorted()[1]

    fun solvePt2(input: List<String>) : Int = intersections(input.map { pathToPoints(it) }).map { it.steps }.sorted()[1]


}