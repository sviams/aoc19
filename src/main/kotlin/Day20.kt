import java.lang.Exception

object Day20 {

    data class NameAndDistance(val name: String, val distance: Int)

    data class PositionOfInterest(val pos: GridPosition, val name: String, val connectsTo: Map<GridPosition, NameAndDistance>)

    fun parseLocationsOfInterest(map: List<String>, w: Int, h: Int) : Pair<List<PositionOfInterest>, Set<GridPosition>> {
        val firstTwo = map.take(2)
        val fromTop = firstTwo.first().foldIndexed(emptyList<PositionOfInterest>()) { index, acc, c ->
            if (c != ' ') acc + PositionOfInterest(index-2 to 0, "$c${firstTwo.last().get(index)}", emptyMap())
            else acc
        }

        val lastTwo = map.takeLast(2)
        val fromBottom = lastTwo.first().foldIndexed(emptyList<PositionOfInterest>()) { index, acc, c ->
            if (c != ' ') acc + PositionOfInterest(index-2 to h-1, "$c${lastTwo.last().get(index)}", emptyMap())
            else acc
        }
        val extraBarriersFromBottom = fromBottom.map { GridPosition(it.pos.first, it.pos.second+1) }

        val leftTwo = listOf(map.map { it.first() }.joinToString(""), map.map { it.drop(1).first() }.joinToString(""))
        val fromLeft = leftTwo.first().foldIndexed(emptyList<PositionOfInterest>()) { index, acc, c ->
            if (c != ' ') acc + PositionOfInterest(0 to index-2, "$c${leftTwo.last().get(index)}", emptyMap())
            else acc
        }

        val rightTwo = listOf(map.map { it.dropLast(1).last() }.joinToString(""), map.map { it.last() }.joinToString(""))
        val fromRight = rightTwo.first().foldIndexed(emptyList<PositionOfInterest>()) { index, acc, c ->
            if (c != ' ') acc + PositionOfInterest(w-1 to index-2, "$c${rightTwo.last().get(index)}", emptyMap())
            else acc
        }.filter { !it.name.contains('#') }
        val extraBarriersFromRight = fromRight.map { GridPosition(it.pos.first+1, it.pos.second) }

        return fromTop + fromBottom + fromLeft + fromRight to (extraBarriersFromBottom + extraBarriersFromRight).toSet()
    }

    fun parseLocationsOfInterestInside(map: List<String>, thickness: Int, width: Int) : Pair<List<PositionOfInterest>, Set<GridPosition>> {
        val tlOffset = 2 + thickness
        val insideWidth = width - 2 * thickness
        val insideHeight = map.size - 4 - 2 * thickness

        val firstTwo = map.drop(tlOffset).take(2)
        val fromTop = firstTwo.first().drop(tlOffset).take(insideWidth).foldIndexed(emptyList<PositionOfInterest>()) { index, acc, c ->
            if (c != ' ') acc + PositionOfInterest(index + thickness to thickness-1, "$c${firstTwo.last().get(index + tlOffset)}", emptyMap())
            else acc
        }
        val extraBarriersFromTop = fromTop.map { GridPosition(it.pos.first, it.pos.second+1) }

        val lastTwo = map.drop(thickness + insideHeight).take(2)
        val fromBottom = lastTwo.last().drop(tlOffset).take(insideWidth).foldIndexed(emptyList<PositionOfInterest>()) { index, acc, c ->
            if (c != ' ') acc + PositionOfInterest(index + thickness to thickness+insideHeight, "${lastTwo.first().get(index+tlOffset)}$c", emptyMap())
            else acc
        }
        val extraBarriersFromBottom = fromBottom.map { GridPosition(it.pos.first, it.pos.second-1) }

        val leftTwo = listOf(map.map { it.drop(tlOffset).first() }.joinToString(""), map.map { it.drop(tlOffset + 1).first() }.joinToString(""))
        val fromLeft = leftTwo.first().drop(tlOffset).take(insideHeight).foldIndexed(emptyList<PositionOfInterest>()) { index, acc, c ->
            if (c != ' ') acc + PositionOfInterest(thickness-1 to index+thickness, "$c${leftTwo.last().get(index + tlOffset)}", emptyMap())
            else acc
        }.filter { !it.name.contains('#') && !it.name.contains('.')}
        val extraBarriersFromLeft = fromLeft.map { GridPosition(it.pos.first+1, it.pos.second) }

        val rightTwo = listOf(map.drop(2).map { it.drop(thickness + insideWidth ).firstOrNull() ?: ' ' }.joinToString(""), map.drop(2).map { it.drop(thickness + insideWidth+1  ).firstOrNull() ?: ' ' }.joinToString(""))
        val fromRight = rightTwo.first().drop(thickness).take(insideHeight).foldIndexed(emptyList<PositionOfInterest>()) { index, acc, c ->
            if (c != ' ') acc + PositionOfInterest(thickness + insideWidth to thickness + index, "$c${rightTwo.last().get(index + thickness)}", emptyMap())
            else acc
        }.filter { !it.name.contains('#') && !it.name.contains('.') }
        val extraBarriersFromRight = fromRight.map { GridPosition(it.pos.first-1, it.pos.second) }

        return fromTop + fromBottom + fromLeft + fromRight to (extraBarriersFromTop + extraBarriersFromBottom + extraBarriersFromLeft + extraBarriersFromRight).toSet()
    }

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

    fun solvePt1(input: List<String>) : Int {
        val totalWidth = input.drop(2).first().length - 2
        val totalHeight = input.size - 4
        val wallThickness = input.drop(2).indexOfFirst { !(it[it.length/2] == '.' || it[it.length/2] == '#' ) }

        val (outside, extraOutsidePlugs) = parseLocationsOfInterest(input, totalWidth, totalHeight)
        val (inside, extraInsidePlugs) = parseLocationsOfInterestInside(input, wallThickness, totalWidth)

        val barriers: Set<GridPosition> = input.drop(2).dropLast(2).foldIndexed(emptySet<GridPosition>()) { rowIndex, rowAcc, row ->
            rowAcc + row.drop(2).foldIndexed(emptySet<GridPosition>()) { colIndex, colAcc, c -> if (c == '#') colAcc + GridPosition(colIndex, rowIndex) else colAcc }
        } + extraInsidePlugs + extraOutsidePlugs

        val allPois = outside + inside
        val withConnections = allPois.map { it.pos to resolveConnections(it, barriers, allPois.minus(it), totalWidth, totalHeight) }.toMap()
        val start = withConnections.values.first { it.name == "AA" }
        val end = withConnections.values.first { it.name == "ZZ" }
        return walkMaze(withConnections, start, mutableMapOf(), 0).filter { it.key == end.pos }.values.min()!!
    }

    fun solvePt2(input: List<String>) : Int {
        val totalWidth = input.drop(2).first().length - 2
        val totalHeight = input.size - 4
        val wallThickness = input.drop(2).indexOfFirst { !(it[it.length/2] == '.' || it[it.length/2] == '#' ) }

        val (outside, extraOutsidePlugs) = parseLocationsOfInterest(input, totalWidth, totalHeight)
        val (inside, extraInsidePlugs) = parseLocationsOfInterestInside(input, wallThickness, totalWidth)

        val barriers: Set<GridPosition> = input.drop(2).dropLast(2).foldIndexed(emptySet<GridPosition>()) { rowIndex, rowAcc, row ->
            rowAcc + row.drop(2).foldIndexed(emptySet<GridPosition>()) { colIndex, colAcc, c -> if (c == '#') colAcc + GridPosition(colIndex, rowIndex) else colAcc }
        } + extraInsidePlugs + extraOutsidePlugs

        val allPois = outside + inside

        val outsideWithConnections = outside.map { it.pos to resolveConnections(it, barriers, allPois.minus(it), totalWidth, totalHeight) }.toMap()
        val insideWithConnections = inside.map { it.pos to resolveConnections(it, barriers, allPois.minus(it), totalWidth, totalHeight) }.toMap()
        val start = outsideWithConnections.values.first { it.name == "AA" }
        return walkMazeRecursive(outsideWithConnections, insideWithConnections, start, mutableMapOf(), 0, 0)
    }
}