import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

object Day17 {

    fun render(pixels: Map<Pos, Long>) {
        val keys = pixels.keys
        val minRow = keys.minBy { it.y }!!.y
        val maxRow = keys.maxBy { it.y }!!.y
        val minCol = keys.minBy { it.x }!!.x
        val maxCol = keys.maxBy { it.x }!!.x
        System.out.println("$minRow to $maxRow, $minCol to $maxCol")
        (maxRow downTo minRow).forEach { line ->
            (maxCol downTo minCol).forEach { col ->
                val p = Pos(col, line)
                val type = if (isIntersection(p, pixels)) 'O' else pixels[p]?.toChar()
                System.out.print(type)
            }
            System.out.println()
        }
    }

    val NORTH = Pos(0,1)
    val SOUTH = Pos(0,-1)
    val WEST = Pos(-1,0)
    val EAST = Pos(1,0)

    val turnMapCW = mapOf(NORTH to EAST, EAST to SOUTH, SOUTH to WEST, WEST to NORTH)
    val turnMapCCW = mapOf(NORTH to WEST, WEST to SOUTH, SOUTH to EAST, EAST to NORTH)

    val SCAFFOLD = 35L
    val NEWLINE = 10L

    fun isIntersection(pos: Pos, map: Map<Pos, Long>) : Boolean =
        map[pos] == SCAFFOLD &&
        map[pos.plus(NORTH)] == SCAFFOLD &&
            map[pos.plus(SOUTH)] == SCAFFOLD &&
            map[pos.plus(WEST)] == SCAFFOLD &&
            map[pos.plus(EAST)] == SCAFFOLD

    fun parseMap(raw: List<Long>) : Map<Pos, Long> {
        val w = raw.indexOf(NEWLINE) + 1
        return raw.windowed(w,w).mapIndexed { rowIndex, list ->
            list.dropLast(1).mapIndexed { colIndex, type ->
                Pos(colIndex, rowIndex) to type
            }
        }.flatten().toMap()
    }

    tailrec fun walk(map: Map<Pos, Long>, pos: Pos, dir: Pos, steps: Int) : Pair<Pos, String> {
        return if (map[pos.plus(dir)] != SCAFFOLD)
            pos to steps.toString()
        else walk(map, pos.plus(dir), dir, steps + 1)
    }

    tailrec fun parseSteps(map: Map<Pos, Long>, botPos: Pos, botDir: Pos, steps: List<String>): List<String> {
        val canMove = map[botPos.plus(botDir)] == SCAFFOLD
        val left = turnMapCCW[botDir]!!
        val right = turnMapCW[botDir]!!
        val canTurnLeft = map[botPos.plus(left)] == SCAFFOLD
        val canTurnRight = map[botPos.plus(right)] == SCAFFOLD
        if (!canMove && !canTurnLeft && !canTurnRight) return steps
        val (nextPos: Pos, nextMove: String) = if (canMove) {
            walk(map, botPos, botDir, 0)
        } else if (canTurnLeft) {
            botPos to "R" // Yes, coordinates turned around...
        } else {
            botPos to "L"
        }
        val nextDir = if (canMove) botDir else if (canTurnLeft) turnMapCCW[botDir]!! else turnMapCW[botDir]!!
        return parseSteps(map, nextPos, nextDir, steps + nextMove)
    }

    fun solvePt1(program: List<Long>) : Int {
        val map = parseMap(ICC.run(IntCodeComputer.immutable(IntCodeComputer.parse(program), listOf())).output)
        return map.keys.filter { isIntersection(it, map) }.sumBy { it.product() }
    }

    tailrec fun findLargestRepeatingPattern(input: String, bound: Int) : String {
        val b = if (bound > input.length) input.length else bound
        val trial = input.take(b)
        val split = input.split(trial)
        return if (split.size > 2) trial else findLargestRepeatingPattern(input, b - 1)
    }

    fun trimCommas(s: String) : String {
        val withoutLeading = if (s.first() == ',') s.drop(1) else s
        return if (withoutLeading.last() == ',') withoutLeading.dropLast(1) else withoutLeading
    }

    tailrec fun reduceToOps(allSteps: String, result: List<Char>, a: String, b: String, c: String) : String =
        when {
            allSteps.length <= 1 -> result.joinToString(",")
            allSteps.startsWith(a) -> reduceToOps(allSteps.drop(a.length+1), result.plus('A'),a,b,c)
            allSteps.startsWith(b) -> reduceToOps(allSteps.drop(b.length+1), result.plus('B'),a,b,c)
            else -> reduceToOps(allSteps.drop(c.length+1), result.plus('C'),a,b,c)
        }

    fun toInput(s: String) : PersistentList<Long> = (s.toCharArray().map { it.toLong() } + NEWLINE).toPersistentList()

    fun solvePt2(program: List<Long>) : Long {
        val map = parseMap(ICC.run(IntCodeComputer.immutableFrom(program, listOf())).output)
        val startPos = map.filter { it.value == '^'.toLong() }.keys.first()
        val allStepsString = parseSteps(map, startPos, SOUTH, emptyList()).joinToString(",")
        val a = trimCommas(findLargestRepeatingPattern(allStepsString, 20))
        val allStepsWithoutA = trimCommas(allStepsString.split(a).sortedBy { it.length }.last())
        val b = trimCommas(findLargestRepeatingPattern(allStepsWithoutA, 20))
        val c = trimCommas(allStepsWithoutA.split(b).sortedBy { it.length }.last())
        val main = reduceToOps(allStepsString, emptyList(), a,b,c)

        val afterMain = ICC.run(IntCodeComputer.immutableFrom(listOf(2L) + program.drop(1), toInput(main)))
        val afterA = ICC.run(afterMain.copy(input = toInput(a)))
        val afterB = ICC.run(afterA.copy(input = toInput(b)))
        val afterC = ICC.run(afterB.copy(input = toInput(c)))
        val final = ICC.run(afterC.copy(input = toInput("n"), output = persistentListOf()))

        return final.output.last()
    }


}