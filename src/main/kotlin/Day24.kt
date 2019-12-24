
object Day24 {


    val NORTH = Pos(0,1)
    val SOUTH = Pos(0,-1)
    val WEST = Pos(-1,0)
    val EAST = Pos(1,0)

    fun sumAdjacent(state: Map<Pos, Int>, pos: Pos) : Int {
        val above = state[pos.plus(NORTH)] ?: 0
        val below = state[pos.plus(SOUTH)] ?: 0
        val left = state[pos.plus(WEST)] ?: 0
        val right = state[pos.plus(EAST)] ?: 0
        return above + below + left + right
    }

    tailrec fun evolve(state: Map<Pos, Int>, earlier: List<String>) : Map<Pos, Int> {
        val stateString = state.toString()
        if (earlier.contains(stateString)) return state
        val nextState = state.map { (p,v) ->
            val adj = sumAdjacent(state, p)
            val x = if (v == 1 && adj == 1 || (v == 0 && (adj == 1 || adj == 2))) 1 else 0
            p to x
        }.toMap()
        val nextEarlier = earlier + stateString
        return evolve(nextState, nextEarlier)
    }

    fun biodiversity(state: Map<Pos, Int>) : Long {
        val keys = state.keys
        val minRow = keys.minBy { it.y }!!.y
        val maxRow = keys.maxBy { it.y }!!.y
        val minCol = keys.minBy { it.x }!!.x
        val maxCol = keys.maxBy { it.x }!!.x
        return (minRow .. maxRow).fold(0L) { lineAcc, line ->
            (minCol .. maxCol).fold(lineAcc) { colAcc, col ->
                val index = line * 5 + col
                val p = Pos(col, line)
                val atP = state[p]
                colAcc + if (atP == 1) (Math.pow(2.0, index.toDouble())).toLong() else 0L
            }
        }
    }

    fun solvePt1(input: List<String>) : Long {
        val state = input.mapIndexed { rowNo, line ->
            line.mapIndexed { colNo, c ->
                Pos(colNo, rowNo) to if (c == '#') 1 else 0
            }
        }.flatten().toMap()

        val end = evolve(state, emptyList())
        val biodiv = biodiversity(end)
        return biodiv
    }

    fun sumRecursive(state: Map<Int, Map<Pos, Int>>) : Long {
        val keys = state.keys
        val minLevel = keys.min()!!
        val maxLevel = keys.max()!!
        return (minLevel .. maxLevel).fold(0L) { levelAcc, level ->
            (0 .. 4).fold(levelAcc) { lineAcc, line ->
                (0 .. 4).fold(lineAcc) { colAcc, col ->
                    val p = Pos(col, line)
                    val gridAtLevel = state[level] ?: emptyMap()
                    val atP = gridAtLevel[p] ?: 0
                    colAcc + atP
                }
            }
        }

    }

    val bottomRow = (0 .. 4).map { Pos(it, 4) }
    val topRow = (0 .. 4).map { Pos(it, 0) }
    val leftCol = (0 .. 4).map { Pos(0, it) }
    val rightCol = (0 .. 4).map { Pos(4, it) }

    val aboveCenter = Pos(2,1)
    val belowCenter = Pos(2,3)
    val leftCenter = Pos(1,2)
    val rightCenter = Pos(3,2)
    val center = Pos(2,2)

    fun sumPositions(grid: Map<Pos, Int>, toSum: List<Pos>) = grid.entries.filter { toSum.contains(it.key) }.sumBy { it.value }

    fun sumAdjacentRecursive(state: Map<Int, Map<Pos, Int>>, pos: Pos, level: Int) : Int {
        val outside = state[level-1] ?: emptyMap()
        val inside = state[level+1] ?: emptyMap()
        val current = state[level] ?: emptyMap()
        val below = if (pos.y == 4) outside[belowCenter] ?: 0 else  if (pos == aboveCenter) sumPositions(inside, topRow) else current[pos.plus(NORTH)] ?: 0
        val above = if (pos.y == 0) outside[aboveCenter] ?: 0 else if (pos == belowCenter) sumPositions(inside, bottomRow) else current[pos.plus(SOUTH)] ?: 0
        val left = if (pos.x == 0) outside[leftCenter] ?: 0 else if (pos == rightCenter) sumPositions(inside, rightCol) else current[pos.plus(WEST)] ?: 0
        val east = if (pos.x == 4) outside[rightCenter] ?: 0 else if (pos == leftCenter) sumPositions(inside, leftCol) else current[pos.plus(EAST)] ?: 0
        return below + above + left + east
    }

    fun innerEdgeHasBug(grid: Map<Pos, Int>) =
        ((grid[aboveCenter] ?: 0) + (grid[belowCenter] ?: 0) + (grid[leftCenter] ?: 0) + (grid[rightCenter] ?: 0)) > 0

    fun outerEdgeHasBug(grid: Map<Pos, Int>) =
        (sumPositions(grid, topRow) + sumPositions(grid, bottomRow) + sumPositions(grid, leftCol) + sumPositions(grid, rightCol)) > 0

    fun evolveGrid(state: Map<Int, Map<Pos, Int>>, grid: Map<Pos, Int>, level: Int) : Map<Pos, Int> =
        grid.map { (p,v) ->
            if (p == center) p to 0
            else {
                val adj = sumAdjacentRecursive(state, p, level)
                val x = if (v == 1 && adj == 1 || (v == 0 && (adj == 1 || adj == 2))) 1 else 0
                p to x
            }
        }.toMap()

    tailrec fun evolveRecursive(state: Map<Int, Map<Pos, Int>>, minute: Int, limit: Int) : Map<Int, Map<Pos, Int>> {
        if (minute == limit) return state
        val evolvedState = state.map { (l, g) -> l to evolveGrid(state, g, l) }.toMap()
        val lowestLevel = state.keys.min()!!
        val highestLevel = state.keys.max()!!
        val addOutside = outerEdgeHasBug(state[lowestLevel]!!)
        val addInside = innerEdgeHasBug(state[highestLevel]!!)
        val nextStateWithOutside: Map<Int, Map<Pos, Int>> = if (addOutside) evolvedState + (lowestLevel-1 to evolveGrid(state, emptyGrid(), lowestLevel-1)) else evolvedState
        val nextState: Map<Int, Map<Pos, Int>> = if (addInside) nextStateWithOutside + (highestLevel+1 to evolveGrid(state, emptyGrid(), highestLevel+1)) else nextStateWithOutside
        return evolveRecursive(nextState, minute + 1, limit)
    }

    fun emptyGrid() : Map<Pos, Int> =
        (0 .. 4).fold(emptyMap()) { lineAcc, line ->
            (0 .. 4).fold(lineAcc) { colAcc, col ->
                colAcc + (Pos(col, line) to 0)
            }
        }

    fun solvePt2(input: List<String>, limit: Int) : Long {
        val state = input.mapIndexed { rowNo, line ->
            line.mapIndexed { colNo, c ->
                Pos(colNo, rowNo) to if (c == '#') 1 else 0
            }
        }.flatten().toMap()
        val end = evolveRecursive(mapOf(0 to state), 0, limit)
        return sumRecursive(end)
    }

}