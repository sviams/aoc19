fun <T> Sequence<T>.takeWhileInclusive(pred: (T) -> Boolean): Sequence<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = pred(it)
        result
    }
}

fun <T> List<T>.takeWhileInclusive(pred: (T) -> Boolean): List<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = pred(it)
        result
    }
}

fun <T> List<List<T>>.drawSimple() =
    forEach { row ->
        row.forEach { col ->
            System.out.print(if (col == 0L) '.' else '#')
        }
        System.out.println()
    }