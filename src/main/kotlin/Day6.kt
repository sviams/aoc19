import kotlinx.collections.immutable.*

object Day6 {

    tailrec fun buildTree(input: PersistentList<String>, tree: PersistentMap<String, String>) : PersistentMap<String, String> {
        if (input.isEmpty()) return tree
        val (left, right) = """(\S+)\)(\S+)""".toRegex().find(input[0])!!.destructured
        return buildTree(input.removeAt(0), tree.put(right, left))
    }

    tailrec fun pathToRoot(item: String, tree: PersistentMap<String, String>, path: PersistentList<String> = persistentListOf()) : PersistentList<String> {
        if (!tree.containsKey(item)) return path
        val parent = tree[item]!!
        return pathToRoot(parent, tree, path.add(parent))
    }

    fun solvePt1(input: List<String>) : Int {
        val tree = buildTree(input.toPersistentList(), persistentHashMapOf())
        return tree.keys.sumBy { pathToRoot(it, tree).size }
    }

    fun solvePt2(input: List<String>) : Int {
        val tree = buildTree(input.toPersistentList(), persistentHashMapOf())
        val myPath = pathToRoot("YOU", tree).toList()
        val santaPath = pathToRoot("SAN", tree).toList()
        val same = myPath.intersect(santaPath)
        return santaPath.size + myPath.size - 2 * same.size
    }

}