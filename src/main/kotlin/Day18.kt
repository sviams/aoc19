import java.lang.Exception

object Day18 {

    fun distanceTo(map: Map<Pos, Char>, doors: Set<Pos>, from: Pos, to: Pos, w: Int, h: Int) : Pair<List<GridPosition>, Int> {
        val barriers = (map.filter { it.value == '#' }.keys + doors).map { GridPosition(it.x, it.y) }.toSet()
        val grid = SquareGrid(w,h,listOf(barriers))
        val (steps, dist) = try { aStarSearch(from.toGP(), to.toGP(), grid) } catch (e: Exception) { Pair(emptyList<GridPosition>(),Int.MAX_VALUE)}
        return Pair(steps, dist)
    }

    data class KeyInfo(val requires: List<Char>, val distance: Int)

    fun mapKeys(map: Map<Pos, Char>, pos: Pos, keysToFind: Map<Pos, Char>, doors: Map<Pos, Char>, w: Int, h: Int, result: Map<Pos, Map<Char, KeyInfo>> ): Map<Pos, Map<Char, KeyInfo>> {
        if (keysToFind.isEmpty()) return result

        val positionsOfInterest = keysToFind.keys + pos

        return  positionsOfInterest.map { intPos ->
            intPos to keysToFind.minus(intPos).map { (keyPos, keyName) ->
                val (steps, dist) = distanceTo(map, emptySet(), intPos, keyPos, w, h)
                val doorsBetween = steps.drop(1).fold(emptyList<Char>()) { acc, step ->
                    val p = Pos(step.first, step.second)
                    if (doors.containsKey(p)) acc + doors[p]!!.toLowerCase() else acc
                }
                keyName to KeyInfo(doorsBetween, dist)
            }.toMap().filter { it.value.distance < Int.MAX_VALUE }
        }.toMap()
    }

    fun findShortestPath(pos: Pos, keyMap: Map<Pos, Map<Char, KeyInfo>>, keyLookup: Map<Char, Pos>, keysToFind: List<Char>, steps: Int, keysFound: List<Char>, memo: MutableMap<String, Int>): MutableMap<String, Int> {
        if (keysToFind.isEmpty()) return memo
        val others: Map<Char, KeyInfo> = keyMap[pos]!!
        val reachable = others.filter { (keyName, keyInfo) -> !keysFound.contains(keyName) && keysFound.containsAll(keyInfo.requires) }
        reachable.forEach { (rName, rInfo) ->
            val nextPos = keyLookup[rName]!!
            val nextFound = keysFound + rName
            val nextSteps = steps + rInfo.distance
            val keysLeft = keysToFind.minus(nextFound).sorted()
            val memoKey = "$nextPos-$rName-$$keysLeft"
            if (nextSteps < memo[memoKey] ?: Int.MAX_VALUE) {
                memo.put(memoKey, nextSteps)
                findShortestPath(nextPos, keyMap, keyLookup, keysToFind.minus(rName), nextSteps, nextFound, memo)
            }
        }
        return memo
    }

    fun findShortestPath2(positions: Map<Int, Pos>, keyMaps: List<Map<Pos, Map<Char, KeyInfo>>>, keyLookup: Map<Char, Pos>, keysToFind: List<Char>, stepsList: Map<Int, Int>, keysFound: List<Char>, botIndex: Int, memoList: Map<Int, MutableMap<String, Int>>): Map<Int, MutableMap<String, Int>> {
        val memo = memoList[botIndex]!!
        if (keysToFind.isEmpty()) return memoList
        val keyMap: Map<Pos, Map<Char, KeyInfo>> = keyMaps[botIndex]!!
        val pos: Pos = positions[botIndex]!!
        val steps: Int = stepsList[botIndex] ?: 0
        val others: Map<Char, KeyInfo> = keyMap[pos] ?: emptyMap()
        val reachable = others.filter { (keyName, keyInfo) -> !keysFound.contains(keyName) && keysFound.containsAll(keyInfo.requires) }
        if (reachable.isEmpty())
            return findShortestPath2(positions, keyMaps, keyLookup, keysToFind, stepsList, keysFound, (botIndex+1)%4, memoList)
        reachable.forEach { (rName, rInfo) ->
            val nextPos = keyLookup[rName]!!
            val nextPositions = positions.plus(botIndex to nextPos)
            val nextFound = keysFound + rName
            val nextSteps = steps + rInfo.distance
            val nextStepsList = stepsList.plus(botIndex to nextSteps)
            val keysLeft = keysToFind.minus(nextFound).filter { others.containsKey(it) }.sorted()
            val memoKey = "$nextPos-$rName-$$keysLeft"
            if (nextSteps <= memo[memoKey] ?: Int.MAX_VALUE) {
                memo.put(memoKey, nextSteps)
                findShortestPath2(nextPositions, keyMaps, keyLookup, keysToFind.minus(rName), nextStepsList, nextFound, botIndex, memoList)
            }
        }
        return memoList
    }

    fun solvePt1(input: List<String>) : Int {

        val w = input.first().length
        val h = input.size

        val map = input.foldIndexed(emptyMap<Pos, Char>()) { rowIndex, rowAcc, line ->
            rowAcc +  line.foldIndexed(emptyMap<Pos, Char>()) { colIndex, colAcc, c -> colAcc + (Pos(colIndex, rowIndex) to c) }
        }
        val entryPos = map.filter { it.value == '@' }.keys.first()
        val keys: Map<Pos, Char> = map.filter { it.value.isLowerCase() }
        val kLookup = keys.entries.associate { (k,v) -> v to k }
        val doors = map.filter { it.value.isUpperCase() }

        val keyMap = mapKeys(map, entryPos, keys, doors, w, h, emptyMap())
        val end = findShortestPath(entryPos, keyMap, kLookup, keys.values.toList(), 0, emptyList(), mutableMapOf())
        return end.filter { (key, value) -> key.indexOf('[') == key.indexOf(']')-1 }.values.min()!!
    }

    fun solvePt2(input: List<String>) : Int {
        val w = input.first().length
        val h = input.size

        val bigMap: Map<Pos, Char> = input.foldIndexed(emptyMap()) { rowIndex, rowAcc, line ->
            rowAcc +  line.foldIndexed(emptyMap<Pos, Char>()) { colIndex, colAcc, c -> colAcc + (Pos(colIndex, rowIndex) to c) }
        }

        val entryPositions = bigMap.filter { it.value == '@' }.keys.mapIndexed { index, pos -> index to pos }.toMap() //maps.mapIndexed { index, m -> index to m.filter { it.value == '@' }.keys.first() }.toMap()
        val allKeys: Map<Pos, Char> = bigMap.filter { it.value.isLowerCase() }
        val keyLookup = allKeys.entries.associate { (k,v) -> v to k }
        val doors = bigMap.filter { it.value.isUpperCase() }
        val keyMaps = entryPositions.map { (i, p) -> mapKeys(bigMap, p, allKeys, doors, w, h, emptyMap()).filter { it.value.isNotEmpty() } }
        val emptyMemos = mapOf<Int, MutableMap<String, Int>>(0 to mutableMapOf(),1 to mutableMapOf(),2 to mutableMapOf(),3 to mutableMapOf())

        val end = findShortestPath2(entryPositions, keyMaps, keyLookup, allKeys.values.toList(), mapOf(0 to 0,1 to 0,2 to 0,3 to 0), emptyList(), 0, emptyMemos)
        val asd: List<Int> = end.map { (bot, memo) -> memo.filter { (key, value) -> key.indexOf('[') == key.indexOf(']')-1 }.values.min() ?: 0 }
        return asd.sum()
    }
}