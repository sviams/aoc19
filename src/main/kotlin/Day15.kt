import kotlinx.collections.immutable.persistentListOf

object Day15 {

    fun render(pixels: Map<Pos, TypeAndDistance>, player: Pos, dir: Long) {
        val keys = pixels.keys
        val minRow = keys.minBy { it.y }!!.y -1
        val maxRow = keys.maxBy { it.y }!!.y +1
        val minCol = keys.minBy { it.x }!!.x -1
        val maxCol = keys.maxBy { it.x }!!.x +1
        System.out.println("$minRow to $maxRow, $minCol to $maxCol")
        (minRow .. maxRow).forEach { line ->
            (minCol .. maxCol).forEach { col ->
                val type = pixels[Pos(col, line)]?.type
                val toDraw = when (type) {
                    WALL -> '#'
                    OPEN -> '.'
                    GOAL -> 'X'
                    else -> ' '
                }
                val playerIcon = when (dir) {
                    NORTH -> 'v'
                    SOUTH -> '^'
                    EAST -> '>'
                    else -> '<'
                }
                if (Pos(col, line) == player) System.out.print(playerIcon) else System.out.print(toDraw)
            }
            System.out.println()
        }
    }

    val NORTH = 1L
    val SOUTH = 2L
    val WEST = 3L
    val EAST = 4L

    val UP = Pos(0,1)
    val DOWN = Pos(0,-1)
    val LEFT = Pos(-1,0)
    val RIGHT = Pos(1,0)

    val dirMap = mapOf(NORTH to UP, SOUTH to DOWN, WEST to LEFT, EAST to RIGHT)
    val turnMap = mapOf(NORTH to EAST, EAST to SOUTH, SOUTH to WEST, WEST to NORTH)

    val WALL = 0L
    val OPEN = 1L
    val GOAL = 2L

    data class TypeAndDistance(val type: Long, val distance: Long)

    fun turn(map: Map<Pos, TypeAndDistance>, dir: Long, pos: Pos) : Long {
        val right = turnMap[dir]!!
        val back = turnMap[right]!!
        val left = turnMap[back]!!

        val atStraight = map[pos.plus(dirMap[dir]!!)]?.type
        val atRight = map[pos.plus(dirMap[right]!!)]?.type
        val atLeft = map[pos.plus(dirMap[left]!!)]?.type

        if (atLeft != WALL) return left
        if (atStraight != WALL) return dir
        if (atRight != WALL) return right
        return back
    }

    tailrec fun findOrFlood(cpu: ICC, pos: Pos, dir: Long, goal: Long, map: Map<Pos, TypeAndDistance>) : Pair<Map<Pos, TypeAndDistance>, ICC> {
        val nextDir = turn(map, dir, pos)
        val out = ICC.run(cpu.copy(input = persistentListOf(nextDir), output = persistentListOf()))//cpu.copy(input = listOf(nextDir).toMutableList()).run(cpu.lastPos)
        val status = out.output.last()

        val checkPos = pos.plus(dirMap[nextDir]!!)
        val stepsThere = map[pos]!!.distance + 1
        val existingEntry = map[checkPos]
        val newEntry = if (existingEntry != null && existingEntry.distance < stepsThere) existingEntry else TypeAndDistance(status, stepsThere)
        val nextMap = map.plus(checkPos to newEntry)
        val nextPos = if (status == WALL) pos else checkPos
        if (status == goal || nextMap.size == 1654) return Pair(nextMap, out)
        return findOrFlood(out, nextPos, nextDir, goal, nextMap)
    }

    fun solvePt1(program: List<Long>) : Long {
        val startCpu = IntCodeComputer.immutableFrom(program, listOf())
        val (endMap, cpu) = findOrFlood(startCpu, Pos(0,0), NORTH, GOAL, mapOf(Pos(0,0) to TypeAndDistance(OPEN, 0)))
        return endMap.filter { it.value.type == GOAL }.values.first().distance
    }

    fun solvePt2(program: List<Long>) : Long {
        val startCpu = IntCodeComputer.immutableFrom(program, listOf())
        val (halfMap, cpuAtOxygenizer) = findOrFlood(startCpu, Pos(0,0), NORTH, GOAL, mapOf(Pos(0,0) to TypeAndDistance(OPEN, 0)))
        val (secondMap, endCpu) = findOrFlood(cpuAtOxygenizer, Pos(14,-14), NORTH, -1, mapOf(Pos(14,-14) to TypeAndDistance(OPEN, 0)))
        return secondMap.filter { it.value.type == OPEN }.maxBy { it.value.distance }!!.value.distance
    }
}