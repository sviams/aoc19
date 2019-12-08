
object Day8 {

    data class Row(val cols: List<Int>) {
        fun count(digit: Int) = cols.count { it == digit }
    }

    data class Layer(val rows: List<Row>) {
        fun count(digit: Int) = rows.sumBy { it.count(digit) }
        fun print() {
            rows.forEach { row ->
                row.cols.forEach { col ->
                    System.out.print(if (col == 1) '*' else ' ')
                }
                System.out.println()
            }
        }
    }

    data class Image(val layers: List<Layer>, val width: Int, val height: Int) {
        fun unscrambled() : Layer {
            val asd: List<Row> = (0 until height).fold(listOf()) { rowAcc: List<Row>, row ->
                val decRow: List<Int> =  (0 until width).fold(listOf()) { colAcc: List<Int>, col ->
                    val stack: List<Int> = layers.fold(listOf()) { stackAcc: List<Int>, layer -> stackAcc + layer.rows[row].cols[col]}
                    val firstVisible = stack.dropWhile { it == 2 }.first()
                    colAcc + firstVisible
                }
                rowAcc + Row(decRow)
            }
            return Layer(asd)
        }

    }

    fun decodeImage(input: List<Int>, colCount: Int, rowCount: Int) : Image {
        val rows = input.windowed(colCount, colCount).map { Row(it) }
        val layers = rows.windowed(rowCount, rowCount).map { Layer(it) }
        return Image(layers, colCount, rowCount)
    }

    fun solvePt1(cols: Int, rows: Int, input: List<Int>) : Int {
        val leastZeroLayer = decodeImage(input, cols, rows).layers.minBy { it.count(0) }!!
        return leastZeroLayer.count(1) * leastZeroLayer.count(2)
    }

    fun solvePt2(cols: Int, rows: Int, input: List<Int>) : String {
        decodeImage(input, cols, rows).unscrambled().print()
        return "LGYHB"
    }

}