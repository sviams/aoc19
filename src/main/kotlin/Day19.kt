object Day19 {

    fun render(grid: List<List<Long>>) {
        grid.forEach { row ->
            row.forEach { col ->
                System.out.print(if (col == 0L) '.' else '#')
            }
            System.out.println()
        }
    }

    fun calcGrid(prog: ImmutableIntCodeProgram, startX: Long, endX: Long, startY: Long, endY: Long) =
        (startY until endY).map { y ->
            (startX until endX).map { x ->
                ICC.run(IntCodeComputer.immutable(prog, mutableListOf(x,y))).output.last()
            }
        }


    fun solvePt1(program: List<Long>, w: Long, h: Long) : Long =
        calcGrid(IntCodeComputer.parse(program), 0, w, 0, h).fold(0L) { acc, line -> acc + line.sum() }


    tailrec fun firstRowWithWidth(prog: ImmutableIntCodeProgram, startX: Long, endX: Long, targetWidth: Int, row: Long) : Long {
        val r = calcGrid(prog, startX, endX, row, row+1).first()
        val rowWidth = r.sum()
        val nextStartX = startX + r.indexOf(1L)
        return if (rowWidth >= targetWidth) row
        else firstRowWithWidth(prog, nextStartX, nextStartX-5 + targetWidth+5, targetWidth, row+1)
    }

    fun solvePt2(program: List<Long>) : Long {
        val prog = IntCodeComputer.parse(program)
        val lineAt1000 = calcGrid(prog, 0,1000,1000,1001).last()
        val growsWidthPer100 = lineAt1000.sum() / 10 // 10
        val movesXPer100 = lineAt1000.indexOf(1L) / 10 // 66
        val minWidthNeeded = 100 + movesXPer100 // 166
        val finalY = firstRowWithWidth(prog, 0,2000, minWidthNeeded, minWidthNeeded * (growsWidthPer100)-50)
        val oneLineAtFinalY = calcGrid(prog, 0, 2000, finalY, finalY+1)
        val finalX = oneLineAtFinalY.first().indexOf(1L)  + movesXPer100
        return finalX * 10000 + finalY
    }


}