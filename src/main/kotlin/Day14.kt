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

    tailrec fun oreRequiredFor(recipes: List<Recipe>, required: List<Ingredient>, stock: Map<String, Long>) : List<Ingredient> {
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
        return oreRequiredFor(recipes, nextRequired, newStock)
    }

    fun oreRequiredForFuel(recipes: List<Recipe>, fuelAmount: Long) : Long =
        oreRequiredFor(recipes, listOf(Ingredient("FUEL", fuelAmount)), emptyMap()).fold(0L) { acc, p -> acc + p.quantity}

    tailrec fun bisectMax(range: LongRange, limit: Long, knownAtLower: Long = -1, knownAtUpper: Long = -1, fn: (Long) -> Long) : Long {
        val atLower = if (knownAtLower == -1L) fn(range.first) else knownAtLower
        val atUpper = if (knownAtUpper == -1L) fn(range.last) else knownAtUpper
        val boundedUpper = if (atUpper > limit) 0 else atUpper
        val rangeDiff = range.last - range.first
        val nextLower = if (boundedUpper > atLower) range.first + rangeDiff else range.first
        val nextUpper = if (boundedUpper > atLower) range.last*2 else range.last - rangeDiff/2
        val nextKnownLower = if (nextLower == range.first) atLower else if (nextLower == range.last) atUpper else -1L
        val nextKnownUpper = if (nextUpper == range.first) atLower else if (nextUpper == range.last) atUpper else -1L
        if (nextLower == nextUpper-1) return nextLower
        return bisectMax((nextLower .. nextUpper), limit, nextKnownLower, nextKnownUpper, fn)
    }

    fun solvePt1(input: List<String>) : Long = oreRequiredForFuel(input.map(::parseRecipe), 1)

    fun solvePt2(input: List<String>) : Long =
        bisectMax((1 .. 1000000000000L), 1000000000000L) { oreRequiredForFuel(input.map(::parseRecipe), it)}
}