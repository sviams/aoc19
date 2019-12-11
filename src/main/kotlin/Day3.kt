import java.lang.Integer.max
import kotlin.math.min

object Day3 {

    data class Result(val pos: Pos, val steps: Int)

    val CENTRAL = Pos(0,0)

    val xMap = mapOf('D' to 0, 'U' to 0, 'R' to 1, 'L' to -1 )
    val yMap = mapOf('D' to -1, 'U' to 1, 'R' to 0, 'L' to 0 )

    tailrec fun wireToPoints(nodes: List<String>, result: Path, last: Pos) : Path {
        if (nodes.isEmpty()) return result
        val move = nodes.first()
        val dir = move.first()
        val dX = xMap[dir] ?: 0
        val dY = yMap[dir] ?: 0
        val dist = move.substring(1).toInt()
        val newDots = connectDots(last, dX, dY, dist, emptyList())
        val nextLast = Pos(last.x + dist*dX, last.y + dist*dY)
        return wireToPoints(nodes.minus(move), result.plus(newDots), nextLast)
    }

    tailrec fun connectDots(start: Pos, dx: Int, dy: Int, steps: Int, result: Path) : Path {
        if (steps == 0) return result
        val next = Pos(start.x + dx, start.y + dy)
        return connectDots(next, dx, dy, steps-1, result.plus(next))
    }

    fun solvePt1(input: List<String>) : Int {
        val paths = input.map { wireToPoints(it.split(","), listOf(Pos(0,0)), Pos(0,0)) }
        return paths.first().intersect(paths.last()).map { it.distanceTo(CENTRAL) }.sorted()[1]
    }

    fun solvePt2(input: List<String>) : Int {
        val paths = input.map { wireToPoints(it.split(","), listOf(Pos(0,0)), Pos(0,0)) }
        return paths.first().intersect(paths.last()).map { paths.last().indexOf(it) + paths.first().indexOf(it) }.sorted()[1]
    }

}