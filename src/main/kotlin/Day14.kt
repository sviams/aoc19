import kotlin.math.max

object Day14 {

    data class Ingredient(val name: String, val quantity: Long) {
        fun isOre() = name == "ORE"
    }

    data class Recipe(val input: List<Ingredient>, val output: Ingredient)

    fun parseRecipe(line: String) : Recipe {
        val (left, outQuantity, outName) = """(.*) => (\d+) (\S+)""".toRegex().find(line)!!.destructured
        val inputs = left.split(",").map {
            val (q, n) = """(\d+) (\S+)""".toRegex().find(it)!!.destructured
            Ingredient(n,q.toLong())
        }
        return Recipe(inputs, Ingredient(outName, outQuantity.toLong()))
    }

    tailrec fun reverseUntil(recipes: List<Recipe>, required: List<Ingredient>, stock: Map<String, Long>) : List<Ingredient> {
        if (required.all { it.isOre() }) return required

        val target = required.filter { !it.isOre() }.first()
        val alreadyHave = stock[target.name] ?: 0
        val requiredAfterStock = if (target.quantity - alreadyHave < 0) 0 else target.quantity - alreadyHave
        val stockAmountLeft = if (alreadyHave - target.quantity < 0) 0 else alreadyHave - target.quantity

        val recipe = recipes.first { it.output.name == target.name }
        val outQuantity = recipe.output.quantity
        val requiredMultiples = if (requiredAfterStock > 0)
            requiredAfterStock / outQuantity + if (requiredAfterStock % outQuantity == 0L) 0 else 1
        else 0

        val newStock = stock.plus(target.name to stockAmountLeft + requiredMultiples*outQuantity-requiredAfterStock)

        val needed = if (requiredMultiples > 0) recipe.input.map { Ingredient(it.name, it.quantity * requiredMultiples) } else emptyList()
        val nextRequired = required.minus(target).plus(needed)

        return reverseUntil(recipes, nextRequired, newStock)
    }

    tailrec fun binFindMax(recipes: List<Recipe>, current: Long, upper: Long) : Long {
        val atCurrent = reverseUntil(recipes, listOf(Ingredient("FUEL", current)), emptyMap()).fold(0L) {acc, p -> acc + p.quantity}
        val atUpper = reverseUntil(recipes, listOf(Ingredient("FUEL", upper)), emptyMap()).fold(0L) {acc, p -> acc + p.quantity}
        val boundedUpper = if (atUpper > 1000000000000L) 0 else atUpper
        val diff = upper - current
        val nextCurrent = if (boundedUpper > atCurrent) current + diff/2 else current
        val nextUpper = if (boundedUpper > atCurrent) upper*2 else upper - diff/2
        if (nextCurrent == nextUpper-1) return nextCurrent+1
        return binFindMax(recipes, nextCurrent, nextUpper)
    }

    fun solvePt1(input: List<String>) : Long = reverseUntil(input.map(::parseRecipe), listOf(Ingredient("FUEL", 1)), emptyMap()).fold(0L) {acc, p -> acc + p.quantity}

    fun solvePt2(input: List<String>) : Long = binFindMax(input.map(::parseRecipe), 1,1000000000000L)
}