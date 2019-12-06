import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class Pos(val x: Int, val y: Int) {
    fun move(other: Pos) = Pos(x + other.x, y + other.y)
    fun times(other: Pos) = Pos(x * other.x, y * other.y)
    fun distanceTo(other: Pos) = Math.abs(x - other.x) + Math.abs(y - other.y)
    override fun equals(other: Any?): Boolean {
        val o = other as Pos
        return x == o.x && y == o.y
    }
}

typealias Path = List<Pos>

object Day3 {

    data class Result(val pos: Pos, val steps: Int)

    val CENTRAL = Pos(0,0)

    val xMap = mapOf('D' to 0, 'U' to 0, 'R' to 1, 'L' to -1 )
    val yMap = mapOf('D' to -1, 'U' to 1, 'R' to 0, 'L' to 0 )

    fun pathToPoints(input: String) : List<Pos> =
        input.split(",").fold(listOf(Pos(0,0))) { acc, move ->
            val dir = move.first()
            val dX = xMap[dir] ?: 0
            val dY = yMap[dir] ?: 0
            val dist = move.substring(1).toInt()
            (1 .. dist).fold(acc) { dAcc, dDist ->
                val totalDelta = Pos(dX * dDist, dY * dDist)
                dAcc.plus(acc.last().move(totalDelta))
            }
        }

    fun intersections(paths: List<Path>) : ImmutableList<Result> {
        val one = paths.first()
        val other = paths.last()
        return one.foldIndexed(emptyList<Result>()) { index, acc, pos ->
            if (other.contains(pos)) {
                (acc + Result(Pos(pos.x, pos.y), index + other.indexOf(pos)))
            }
            else acc
        }.toImmutableList()
    }

    fun solvePt1(input: List<String>) : Int = intersections(input.map { pathToPoints(it) }).map { it.pos.distanceTo(CENTRAL) }.sorted()[1]

    fun solvePt2(input: List<String>) : Int {
        val asd = intersections(input.map { pathToPoints(it) })
        val wer = asd.map { it.steps }.sorted()
        return wer[1]
    }


}