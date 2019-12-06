import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

object Day6 {

    data class TreeNode(val Name: String, val children: MutableList<TreeNode>, val indirect: Int, val direct: Int)

    fun findParentAndInsert(parent: String, child: String, current: TreeNode, indirect: Int, direct: Int): Pair<Int, Int> {
        if (current.Name == "ROOT" && current.children.size == 0) {
            current.children.add(TreeNode(parent, mutableListOf(TreeNode(child, mutableListOf(), 0, 1)), 0, 0))
            return Pair(0,1)
        }
        if (current.Name == parent) {
            current.children.add(TreeNode(child, emptyList<TreeNode>().toMutableList(), indirect - 1, direct + 1))
            return Pair(indirect-1, direct + 1)
        }
        return current.children.fold(Pair(indirect, direct)) { acc, c ->
            val res = findParentAndInsert(parent, child, c, indirect + 1, direct)
            if (res.second > acc.second)
                Pair(res.first, res.second)
            else acc
        }
    }

    fun buildTree(input: List<String>, tree: TreeNode, allInput: List<String>) : TreeNode {
        if (input.isEmpty()) return tree
        val asd = input.takeWhileInclusive { line ->
            val (left, right) = """(\S+)\)(\S+)""".toRegex().find(line)!!.destructured
            val parentExistsInTree = hasNode(left, tree)
            !(parentExistsInTree || isRoot(line, allInput))
        }
        val nextLine = asd.last()
        val (left, right) = """(\S+)\)(\S+)""".toRegex().find(nextLine)!!.destructured
        if (isRoot(nextLine, allInput)) {
            tree.children.add(TreeNode(left, mutableListOf(TreeNode(right, mutableListOf(), 0, 1)), 0, 0))
        } else
            findParentAndInsert(left, right, tree, 0, 0)

        return buildTree(input.minus(nextLine), tree, allInput)
    }

    fun sumOrbits(node: TreeNode) : Pair<Int, Int> {
        val tot = node.children.fold(Pair(0,0)) { acc, c ->
            val cVal = sumOrbits(c)
            Pair(acc.first + cVal.first, acc.second + cVal.second)
        }
        return Pair(node.direct + tot.first, node.indirect + tot.second)
    }

    fun hasNode(name: String, tree: TreeNode): Boolean {
        return tree.Name == name || tree.children.any { hasNode(name, it) }
    }

    fun isRoot(line: String, list: List<String>): Boolean {
        val root = line.split(")")[0]
        return list.minus(line).none { it.split(")")[1] == root }
    }

    fun pathToRoot(node: TreeNode, target: String, acc: ImmutableList<String>) : List<String> {
        if (node.children.any { it.Name == target }) return acc + target
        return node.children.fold(emptyList()) { facc, c ->
            val res = pathToRoot(c, target, acc.plus(c.Name).toImmutableList())
            if (res.contains(target)) return res
            acc
        }
    }

    fun solvePt1(input: List<String>) : Int {
        val tree = buildTree(input, TreeNode("ROOT", emptyList<TreeNode>().toMutableList(),0,0), input)
        val apa = sumOrbits(tree)
        val res1 = apa.first + apa.second
        return res1
    }

    fun solvePt2(input: List<String>) : Int {
        val tree = buildTree(input, TreeNode("ROOT", emptyList<TreeNode>().toMutableList(),0,0), input)
        val myPath = pathToRoot(tree, "YOU", emptyList<String>().toImmutableList())
        val santaPath = pathToRoot(tree, "SAN", emptyList<String>().toImmutableList())
        val commonPart = myPath.foldIndexed(0) { index, acc, node ->
            if (santaPath.size > index && santaPath[index] == node) acc + 1 else acc
        }

        return santaPath.size + myPath.size - 2*commonPart - 2
    }

}