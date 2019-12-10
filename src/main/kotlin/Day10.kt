import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day10 {

    data class Pos(val x: Int, val y: Int) {
        fun minus(other: Pos) = Pos(x - other.x, y - other.y)
        fun plus(other: Pos) = Pos(x + other.x, y + other.y)
    }

    fun parseGrid(input: List<String>) : List<List<Int>> =
        input.map { line -> line.map { if (it == '#') 1 else 0 } }

    fun printGrid(grid: List<List<Int>>) =
        grid.forEach { line ->
            line.forEach { col ->
                System.out.print(if (col <= 0) '.' else col)
            }
            System.out.println()
        }


    fun nextPos(current: Pos, w: Int, h: Int): Pos? {
        val nx = (current.x + 1) % w
        val ny = if (nx == 0) current.y+1 else current.y
        return if (ny < h) Pos(nx, ny) else null
    }

    fun isOccluded(startPos: Pos, asteroid: Pos, w: Int, h: Int, grid: List<List<Int>>) : Boolean {
        val dx = startPos.x - asteroid.x
        val dy = startPos.y - asteroid.y
        val absX = abs(dx)
        val absY = abs(dy)
        val min = min(absX, absY)
        val max = max(absX, absY)
        val factor = (1 .. max).fold(1) { acc, i -> if (max % i == 0 && min % i == 0) i else acc }
        val newDx = dx / factor
        val newDy = dy / factor
        val delta = Pos(newDx, newDy)
        val pathToViewPoint = generateSequence(asteroid) { p -> p.plus(delta) }.takeWhile { (it.x in 0 until w && it.y in 0 until h) && it != startPos }.toList()
        return pathToViewPoint.minus(startPos).minus(asteroid).any { grid[it.y][it.x] > 0 }
    }

    tailrec fun checkPos(startPos: Pos, currentPos: Pos?, tl: Pos, br: Pos, visible: List<Pos>, occluded: List<Pos>, grid: List<List<Int>> ) : List<Pos> {
        val w = grid.first().size
        val h = grid.size
        if (currentPos == null) return visible
        val nextPos = nextPos(currentPos, br.x, br.y)
        if (currentPos == startPos) return checkPos(startPos, nextPos, tl, br, visible, occluded, grid)
        if (isOccluded(startPos, currentPos, w,h,grid)) return checkPos(startPos, nextPos, tl, br, visible, occluded, grid)
        if (grid[currentPos.y][currentPos.x] > 0) {
            return checkPos(startPos, nextPos, tl, br, visible + currentPos,  occluded, grid)
        }
        return checkPos(startPos, nextPos, tl, br, visible, occluded, grid)
    }

    fun solvePt1(input: List<String>) : Int {
        val grid = parseGrid(input)
        return grid.mapIndexed { row, line ->
            line.mapIndexed { col, asd ->
                if (asd > 0) checkPos(Pos(col, row), Pos(0,0), Pos(0,0), Pos(grid.first().size,grid.size), emptyList(), emptyList(), grid).size
                else 0
            }
        }.flatten().max()!!
    }

    tailrec fun lazorizeUntilDeath(grid: List<List<Int>>, lazored: List<Pos>, station: Pos): List<Pos> {
        if (lazored.size == 200) return lazored
        val visible: List<Pos> = checkPos(station, Pos(0,0), Pos(0,0), Pos(grid.first().size, grid.size), emptyList(), emptyList(), grid)
        if (visible.isEmpty()) return lazored
        val nextGrid = grid.mapIndexed { row, line ->
            line.mapIndexed { col, existing -> if (visible.contains(Pos(col, row))) 0 else existing }
        }
        val sorted = visible.sortedBy { Math.atan2((it.y-station.y).toDouble(), (it.x - station.x).toDouble()) }
        val firstQ = sorted.takeWhile { it.x < station.x }
        return lazorizeUntilDeath(nextGrid, lazored + sorted.drop(firstQ.size) + firstQ, station)
    }

    fun solvePt2(input: List<String>, station: Pos) : Int {
        val grid = parseGrid(input)
        val deathList = lazorizeUntilDeath(grid, emptyList(), station)
        val winner = deathList[199]
        return winner.x*100 + winner.y
    }

}