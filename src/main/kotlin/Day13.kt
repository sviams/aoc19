
typealias Screen = MutableList<MutableList<Int>>

object Day13 {

    fun render(pixels: Screen) =
        pixels.forEach { line ->
            line.forEach { col ->
                val c = when (col) {
                    0 -> ' '
                    1 -> '|'
                    2 ->  '#'
                    3 -> '_'
                    else -> '*'
                }
                System.out.print(c)
            }
            System.out.println()
        }

    data class GameOutput(val ball: Int, val paddle: Int, val score: Int)

    tailrec fun play(cpu: MutableIntCodeComputer, playerInput: Long, score: Int) : Int {
        if (cpu.lastOp == 99L && score > 0) return score
        val out = cpu.copy(input = listOf(playerInput).toMutableList()).run(0)
        val game = out.output.windowed(3,3).fold(GameOutput(-1, -1, 0)) { acc, p: List<Long> ->
            val x = p[0].toInt()
            val t = p[2].toInt()
            if (t == 3) acc.copy(paddle = x)
            else if (t == 4) acc.copy(ball = x)
            else if (x == -1 && p[1].toInt() == 0) acc.copy(score = t)
            else acc
        }

        val nextInput = when {
            game.ball > game.paddle -> 1L
            else -> -1L
        }

        return play(out.copy(output = mutableListOf()), nextInput, game.score)
    }

    fun solvePt1(program: List<Long>) : Int =
        IntCodeComputer.mutable(IntCodeComputer.parse(program), listOf()).run(0).output.windowed(3,3).count { it[2] == 2L }

    fun solvePt2(program: List<Long>) : Int {
        val startCpu = IntCodeComputer.mutable(IntCodeComputer.parse(listOf(2L) + program.drop(1)), listOf())
        return play(startCpu, 0L, -1)
    }
}