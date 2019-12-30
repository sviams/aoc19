import kotlinx.collections.immutable.*
import java.lang.IllegalArgumentException
import kotlin.math.abs

data class Quad<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
) {
    override fun toString(): String = "($first, $second, $third, $fourth)"

}

fun <T> Quad<T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth)

data class Pos(val x: Int, val y: Int) {
    fun plus(other: Pos) = Pos(x + other.x, y + other.y)
    fun minus(other: Pos) = Pos(x - other.x, y - other.y)
    fun times(other: Pos) = Pos(x * other.x, y * other.y)
    fun distanceTo(other: Pos) = abs(x - other.x) + abs(y - other.y)
    fun product() = x * y
    fun neighbors() = persistentHashSetOf(this.plus(NORTH), this.plus(SOUTH), this.plus(EAST), this.plus(WEST))
    override fun equals(other: Any?): Boolean {
        val o = other as Pos
        return x == o.x && y == o.y
    }

    fun isInFirstQuadrant(): Boolean = x >= 0 && y >= 0

    companion object {
        // Assuming TL is origin
        val NORTH = Pos(0,-1)
        val SOUTH = Pos(0,1)
        val WEST = Pos(-1,0)
        val EAST = Pos(1, 0)
    }
}

typealias Path = PersistentList<Pos>

object AStar {

    private const val MAX_SCORE = 99999999

    private tailrec fun generatePath(currentPos: Pos, cameFrom: PersistentMap<Pos, Pos>, result: Path = persistentListOf()): Path {
        val nextResult = result.add(0, currentPos)
        if (!cameFrom.containsKey(currentPos)) return nextResult
        val nextCurrent = cameFrom.getValue(currentPos)
        return generatePath(nextCurrent, cameFrom, nextResult)
    }

    private tailrec fun checkNeighbors(neighbors: PersistentList<Pos>, current: Pos, end: Pos, o: PersistentSet<Pos>, cfs: PersistentMap<Pos, Int>, cf: PersistentMap<Pos, Pos>, ectf: PersistentMap<Pos, Int>)
        : Quad<PersistentSet<Pos>, PersistentMap<Pos, Int>, PersistentMap<Pos, Pos>, PersistentMap<Pos, Int>> {
        if (neighbors.isEmpty()) return Quad(o, cfs, cf, ectf)
        val neighbor = neighbors[0]
        val score = cfs.getValue(current) + 1
        val isBetter = score < cfs.getOrDefault(neighbor, MAX_SCORE)
        val no = if (isBetter && !o.contains(neighbor)) o.add(neighbor) else o
        val ncf = if (isBetter) cf.put(neighbor, current) else cf
        val ncfs = if (isBetter) cfs.put(neighbor, score) else cfs
        val nectf = if (isBetter) ectf.put(neighbor, score + neighbor.distanceTo(end)) else ectf
        return checkNeighbors(neighbors.removeAt(0), current, end, no, ncfs, ncf, nectf)
    }

    private tailrec fun walk(
        end: Pos,
        open: PersistentSet<Pos>,
        closed: PersistentSet<Pos>,
        costFromStart: PersistentMap<Pos, Int>,
        estimatedCostToFinish: PersistentMap<Pos, Int>,
        cameFrom: PersistentMap<Pos, Pos>)
    : Path {
        if (open.isEmpty()) throw IllegalArgumentException("No path to $end")
        val current = open.minBy { estimatedCostToFinish.getValue(it) }!!
        if (current == end) return generatePath(end, cameFrom)
        val openWithoutCurrent: PersistentSet<Pos> = open.remove(current)
        val closedWithCurrent = closed.add(current)
        val openNeighbors = current.neighbors().filter { it.isInFirstQuadrant() }.filterNot { closedWithCurrent.contains(it) }.toPersistentList()
        val (nextOpen, nextCostFromStart, nextCameFrom, nextEstimatedCostToFinish) = checkNeighbors(openNeighbors, current, end, openWithoutCurrent, costFromStart, cameFrom, estimatedCostToFinish)
        return walk(end, nextOpen, closedWithCurrent, nextCostFromStart, nextEstimatedCostToFinish, nextCameFrom)
    }

    fun shortestPath(from: Pos, to: Pos, barriers: ImmutableSet<Pos>): Path =
        walk(to, persistentHashSetOf(from), barriers.toPersistentHashSet(), persistentHashMapOf(from to 0), persistentHashMapOf(from to from.distanceTo(to)), persistentHashMapOf())
}


fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b
fun lcm(a: Long, b: Long, c: Long) = lcm(a, lcm(b,c))

fun egcd(a: Long, b: Long): Triple<Long, Long, Long> {
    if (a == 0L) return Triple(b, 0, 1)
    val (g,y,x)  = egcd(b % a, a)
    val floor = Math.floorDiv(b, a)
    return Triple(g, x - floor * y, y)
}

fun modInv(a: Long, m: Long): Long {
    val (g,x,y) = egcd(a,m)
    if (g != 1L) throw IllegalArgumentException("Modular inverse does not exist for $a and $m")
    return x % m
}
