object Day11 {

    data class Pos(val x: Int, val y: Int) {
        fun minus(other: Pos) = Pos(x - other.x, y - other.y)
        fun plus(other: Pos) = Pos(x + other.x, y + other.y)
    }

    fun printHull(white: Set<Pos>) {
        val xMin: Int = white.minBy { it.x }!!.x
        val xMax: Int = white.maxBy { it.x }!!.x
        val yMin: Int = white.minBy { it.y }!!.y
        val yMax: Int = white.maxBy { it.y }!!.y
        (yMax downTo yMin).forEach { row ->
            (xMin .. xMax).forEach { col ->
                if (white.contains(Pos(col, row))) System.out.print('#')
                else System.out.print(' ')
            }
            System.out.println()
        }
    }

    val LEFT = Pos(-1,0)
    val DOWN = Pos(0,-1)
    val RIGHT = Pos(1, 0)
    val UP = Pos(0, 1)

    fun left(currentDir: Pos) : Pos {
        return when (currentDir) {
            LEFT -> DOWN
            DOWN -> RIGHT
            RIGHT -> UP
            else -> LEFT
        }
    }

    fun right(currentDir: Pos) : Pos {
        return when (currentDir) {
            LEFT -> UP
            UP -> RIGHT
            RIGHT -> DOWN
            else -> LEFT
        }
    }
    
    tailrec fun drive(cpu: MutableIntCodeComputer, currentPos: Pos, currentDir: Pos, black: Set<Pos>, white: Set<Pos>) : Pair<Set<Pos>, Set<Pos>> {
        if (cpu.lastOp == 99L)
            return Pair(black, white)
        val out = cpu.run(cpu.lastPos)
        val output = out.output.takeLast(2)
        val color = output.first()
        val newWhite = if (color == 1L) white + currentPos else white - currentPos
        val newBlack = if (color == 0L) black + currentPos else black - currentPos
        val newDir = when (output.last()) {
            0L -> left(currentDir)
            else -> right(currentDir)
        }
        val newPos = currentPos.plus(newDir)
        return drive(out.copy(input = if (newWhite.contains(newPos)) listOf(1L).toMutableList() else listOf(0L).toMutableList()), newPos, newDir, newBlack, newWhite)
    }


    fun solvePt1(program: List<Long>) : Int {
        val startCpu = IntCodeComputer.mutable(IntCodeComputer.parse(program), listOf(0))
        val painted = drive(startCpu, Pos(0,0), UP, emptySet(), emptySet())
        return painted.first.size + painted.second.size
    }

    fun solvePt2(program: List<Long>) : String {
        val startCpu = IntCodeComputer.mutable(IntCodeComputer.parse(program), listOf(1))
        val painted = drive(startCpu, Pos(0,0), UP, emptySet(), setOf(Pos(0,0)))
        printHull(painted.second)
        return "URCAFLCP"
    }



}