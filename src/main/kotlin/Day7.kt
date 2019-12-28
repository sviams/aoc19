import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

object Day7 {

    tailrec fun runUntilHalt(amps: PersistentList<ICC>, index: Int, program: ImmutableIntCodeProgram) : Long {
        val prevIndex = if (index == 0) 4 else index - 1
        val prevAmp = amps[prevIndex]
        if (amps.all { it.lastOp == 99L }) return prevAmp.output.last()
        val amp = amps[index]

        val withInput = if (prevAmp.output.isNotEmpty()) amp.copy(input = amp.input.add(prevAmp.output.last()))
        else if (index == 0 && amp.state == program) amp.copy(input = amp.input.add(0))
        else amp

        val maybeRun = if (withInput.input.isNotEmpty() && withInput.lastOp != 99L) ICC.run(withInput) else withInput//amp.run(amp.lastPos) //.runIntCode(amp.lastPos)
        val nextIndex = (index+1) % 5
        return runUntilHalt(amps.set(index, maybeRun), nextIndex, program)
    }

    tailrec fun findMaxOutput(program: ImmutableIntCodeProgram, ampSettings: List<Long>, maxOutput: Long, settingOffset: Long): Long {
        if (ampSettings == listOf(0L,1L,2L,3L,4L).map { it + settingOffset } && maxOutput > 0) return maxOutput
        val amps = (0 until 5).map { IntCodeComputer.immutable(program, persistentListOf(ampSettings[it])) }.toPersistentList() //Amp(program.toMutableList(), mutableListOf(), mutableListOf(ampSettings[it]), 0, 0, 0)
        val output = runUntilHalt(amps, 0, program)
        val nextMax = if (output > maxOutput) output else maxOutput
        val nextSettings = nextSettings(ampSettings, settingOffset)
        return findMaxOutput(program, nextSettings, nextMax, settingOffset)
    }

    tailrec fun nextSettings(last: List<Long>, settingOffset: Long) : List<Long> {
        val lastSettings = last.map { it - settingOffset }.joinToString("").toInt(5)
        val lastChars = lastSettings.plus(1).toString(5).toCharArray()
        val nextSettings = lastChars.map { it.toLong() -48 }
        val f = (listOf(0L,0L,0L,0L,0L) + nextSettings).takeLast(5).map { it + settingOffset }
        return if (f.distinct().size == f.size) f
        else nextSettings(f, settingOffset)
    }

    fun solvePt1(input: List<Long>) = findMaxOutput(IntCodeComputer.parse(input), listOf(0L,1,2,3,4), 0L, 0L)

    fun solvePt2(input: List<Long>) = findMaxOutput(IntCodeComputer.parse(input), listOf(5,6,7,8,9), 0L, 5L)
}