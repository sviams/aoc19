import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

object Day23 {
    
    tailrec fun executeWithNat(cpus: Map<Long, ICC>, q: Map<Long, List<Long>>, sentYs: List<Long>, i: Long, emptyCount: Int) : List<Long> {
        val nats = q[255] ?: emptyList()
        if (sentYs.size > 2 && sentYs[sentYs.size-2] == sentYs[sentYs.size -1]) return sentYs
        val natIntervention = q.minus(255).isEmpty() && emptyCount >= cpus.size
        val index = if (natIntervention) 0 else i
        val queue = if (natIntervention) q + (0L to listOf(nats.last())) else q
        val toRun = cpus[index]!!
        val input = if (natIntervention) nats.windowed(2,2).last() else if (queue.containsKey(index)) queue[index]!! else listOf(-1L)
        val nextSentYs = if (natIntervention) sentYs + input.last() else sentYs
        val queueAfterInput = queue.minus(index)
        val afterRun = ICC.run(toRun.copy(input = input.toPersistentList()))
        val outgoing = afterRun.output.windowed(3,3).map { l -> l[0] to listOf(l[1], l[2]) }
        val nextQueue = outgoing.fold(queueAfterInput) {acc, (key, values) ->
            val existingQ = acc[key] ?: emptyList()
            val newQ = existingQ + values
            acc + (key to newQ)
        }
        val nextEmptyCount = if (nextQueue.minus(255).isEmpty() && !natIntervention) emptyCount + 1 else 0
        val nextIndex = (index + 1) % cpus.size
        val nextCpus = cpus + (index to afterRun.copy(output = persistentListOf()))
        return executeWithNat(nextCpus, nextQueue, nextSentYs, nextIndex, nextEmptyCount)
    }

    fun solvePt1(program: ImmutableIntCodeProgram) : Long {
        val cpus = (0 until 50L).map { it to ICC.run(IntCodeComputer.immutable(program, persistentListOf(it))) }.toMap()
        return executeWithNat(cpus, emptyMap(), emptyList(), 0, 0).first()
    }

    fun solvePt2(program: ImmutableIntCodeProgram) : Long {
        val cpus = (0 until 50L).map { it to ICC.run(IntCodeComputer.immutable(program, listOf(it))) }.toMap()
        return executeWithNat(cpus, emptyMap(), emptyList(), 0, 0).last()
    }


}