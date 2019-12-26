import java.lang.Exception

object Day20 {

    data class NameAndDistance(val name: String, val distance: Int)

    data class PositionOfInterest(val pos: GridPosition, val name: String, val connectsTo: Map<GridPosition, NameAndDistance>)

    val NORTH = GridPosition(0,-1)
    val SOUTH = GridPosition(0,1)
    val WEST = GridPosition(-1,0)
    val EAST = GridPosition(1,0)

    fun poiAtPos(map: List<String>, pos: GridPosition): Pair<PositionOfInterest, GridPosition>? =
        listOf(NORTH, SOUTH, EAST, WEST).fold(emptyList<Pair<PositionOfInterest, GridPosition>>()) { acc, dir ->
            val checkPos = GridPosition(pos.first + dir.first + 2, pos.second + dir.second + 2)
            val atPos = map.get(pos.second + 2).get(pos.first + 2)
            if (atPos == '.') {
                val atDir = map.get(pos.second + dir.second + 2).get(pos.first + dir.first + 2)
                if (atDir.isLetter()) {
                    val otherLetter = map.get(pos.second + dir.second*2 + 2).get(pos.first + dir.first*2 + 2)
                    val name = if (dir.first == -1 || dir.second == -1) listOf(otherLetter, atDir) else listOf(atDir, otherLetter)
                    acc + (PositionOfInterest(pos, name.joinToString(""), emptyMap()) to GridPosition(checkPos.first-2, checkPos.second-2))
                } else acc
            }
            else acc
        }.firstOrNull()

    fun distanceTo(barriers: Set<GridPosition>, from: GridPosition, to: GridPosition, w: Int, h: Int) : Pair<List<GridPosition>, Int> {
        val grid = SquareGrid(w,h,listOf(barriers))
        val (steps, dist) = try { aStarSearch(from, to, grid) } catch (e: Exception) { Pair(emptyList<GridPosition>(),Int.MAX_VALUE)}
        return Pair(steps, dist)
    }

    fun resolveConnections(item: PositionOfInterest, barriers: Set<GridPosition>, others: List<PositionOfInterest>, w: Int, h: Int) : PositionOfInterest {
        val connections: Map<GridPosition, NameAndDistance> = others.map { other ->
            val (steps, dist) = distanceTo(barriers, item.pos, other.pos, w, h)
            other.pos to NameAndDistance(other.name, dist)
        }.toMap().filter { it.value.distance < Int.MAX_VALUE }
        return item.copy(connectsTo = connections)
    }

    fun teleport(pois: Map<GridPosition, PositionOfInterest>, from: PositionOfInterest) : PositionOfInterest {
        val others = pois.minus(from.pos)
        return if (others.any { it.value.name == from.name }) others.filter { it.value.name == from.name }.entries.first().value
        else from
    }

    fun walkMaze(pois: Map<GridPosition, PositionOfInterest>, start: PositionOfInterest, memo: MutableMap<GridPosition, Int>, steps: Int) : MutableMap<GridPosition, Int> {
        if (start.name == "ZZ") return memo

        start.connectsTo.forEach { (otherPos, info) ->
            val nextPos = teleport(pois, pois[otherPos]!!)

            val nextSteps = if (nextPos.pos != otherPos) steps + info.distance + 1 else steps + info.distance

            val cachedSteps: Int = memo.get(nextPos.pos) ?: Int.MAX_VALUE
            if (nextSteps <= cachedSteps) {
                memo.put(nextPos.pos, nextSteps)
                walkMaze(pois, nextPos, memo, nextSteps)
            }
        }
        return memo
    }

    fun teleportRecursive(outside: Map<GridPosition, PositionOfInterest>, inside: Map<GridPosition, PositionOfInterest>, from: PositionOfInterest, level: Int) : Pair<PositionOfInterest, Int>{
        return if (outside.any { it.value.name == from.name && it.key != from.pos}) outside.filter { it.value.name == from.name }.entries.first().value to level + 1
        else if (inside.any { it.value.name == from.name && it.key != from.pos }) inside.filter { it.value.name == from.name }.entries.first().value to level - 1
        else from to level
    }



    fun walkMazeRecursive(outside: Map<GridPosition, PositionOfInterest>, inside: Map<GridPosition, PositionOfInterest>, start: PositionOfInterest, memo: MutableMap<String, Int>, steps: Int, level: Int) : Int {
        if (start.name == "ZZ") return steps
        val totalPortals = (outside.size + inside.size) / 2
        val totalSteps = (outside + inside).entries.sumBy { it.value.connectsTo.values.sumBy { it.distance } }
        val aa: PositionOfInterest = outside.values.first { it.name == "AA" }
        val zz = outside.values.first { it.name == "ZZ" }

        val conns = if (level == 0) start.connectsTo.filter { (pos, info) -> pos == aa.pos || pos == zz.pos || inside.containsKey(pos) } else start.connectsTo.filter { (pos, info) -> pos != aa.pos && pos != zz.pos}

        val dirs = conns.map { (otherPos, info: NameAndDistance) ->
            val otherInfo = (outside + inside)[otherPos]!!
            val (nextPos, nextLevel) = teleportRecursive(outside, inside, otherInfo, level)
            val nextSteps = if (nextPos.pos != otherPos) steps + info.distance + 1 else steps + info.distance
            if (nextLevel < totalPortals && nextSteps < totalSteps*2) {
                walkMazeRecursive(outside, inside, nextPos, memo, nextSteps, nextLevel)
            } else 0
        }.filter { it != 0 }
        return dirs.min() ?: 0
    }

    fun solveMaze(input: List<String>, recursive: Boolean) : Int {
        val totalWidth = input.drop(2).first().length - 2
        val totalHeight = input.size - 4

        val poisAndBarriers = (0 until totalHeight).fold(emptyList<Pair<PositionOfInterest, GridPosition>>()) { rowAcc, row ->
            (0 until totalWidth).fold(rowAcc) { colAcc, col ->
                val p = GridPosition(col, row)
                val maybePoiAndPlug = poiAtPos(input, p)
                if (maybePoiAndPlug != null) colAcc + maybePoiAndPlug else colAcc
            }
        }

        val outsideWithPlugs = poisAndBarriers.filter {
            val (x,y) = it.first.pos
            x == 0 || x == totalWidth-1 || y == 0 || y == totalHeight-1
        }

        val insideWithPlugs = poisAndBarriers.filter { !outsideWithPlugs.contains(it) }
        val outside = outsideWithPlugs.map { it.first }
        val inside = insideWithPlugs.map { it.first }
        val outPlugs = outsideWithPlugs.map { it.second }.filter { it.first >= 0 && it.second >= 0 }
        val inPlugs = insideWithPlugs.map { it.second }.filter { it.first >= 0 && it.second >= 0 }

        val barriers: Set<GridPosition> = input.drop(2).dropLast(2).foldIndexed(emptySet<GridPosition>()) { rowIndex, rowAcc, row ->
            rowAcc + row.drop(2).foldIndexed(emptySet<GridPosition>()) { colIndex, colAcc, c -> if (c == '#') colAcc + GridPosition(colIndex, rowIndex) else colAcc }
        } + outPlugs + inPlugs

        val allPois = outside + inside

        val outsideWithConnections = outside.map { it.pos to resolveConnections(it, barriers, allPois.minus(it), totalWidth, totalHeight) }.toMap()
        val insideWithConnections = inside.map { it.pos to resolveConnections(it, barriers, allPois.minus(it), totalWidth, totalHeight) }.toMap()
        val start = outsideWithConnections.values.first { it.name == "AA" }
        val end = outsideWithConnections.values.first { it.name == "ZZ" }
        return if (recursive) walkMazeRecursive(outsideWithConnections, insideWithConnections, start, mutableMapOf(), 0, 0)
        else walkMaze(outsideWithConnections + insideWithConnections, start, mutableMapOf(), 0).filter { it.key == end.pos }.values.min()!!
    }

    fun solvePt1(input: List<String>) : Int = solveMaze(input, false)

    fun solvePt2(input: List<String>) : Int = solveMaze(input, true)

}