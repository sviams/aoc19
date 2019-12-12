import kotlin.math.abs

object Day12 {

    data class Pos3D(val x: Int, val y: Int, val z: Int) {
        fun plus(other: Pos3D) = Pos3D(x + other.x, y + other.y, z + other.z)
        override fun toString(): String = "$x, $y, $z"
    }

    data class Moon(val pos: Pos3D, val velocity: Pos3D, val id: Int) {
        fun potential() = abs(pos.x) + abs(pos.y) + abs(pos.z)
        fun kinetic() = abs(velocity.x) + abs(velocity.y) + abs(velocity.z)
        fun total() = potential() * kinetic()
        override fun equals(other: Any?): Boolean {
            val o = other as Moon
            return o.id == id
        }

        override fun toString(): String = "$id $pos $velocity"
    }

    fun parseMoon(index: Int, line: String) : Moon {
        val (px, py, pz) = """<x=(\S+), y=(\S+), z=(\S+)>""".toRegex().find(line)!!.destructured
        return Moon(Pos3D(px.toInt(), py.toInt(), pz.toInt()), Pos3D(0,0,0), index)
    }

    fun applyGravity(moon: Moon, others: List<Moon>) : Moon {
        val newVel = others.fold(moon.velocity) { acc, other -> applyGravityTo(moon, other) }
        return moon.copy(velocity = newVel)
    }

    fun applyGravityTo(one: Moon, from: Moon) : Pos3D {
        val newOneXvel: Int = if (from.pos.x > one.pos.x) one.velocity.x + 1 else if (from.pos.x < one.pos.x) one.velocity.x - 1 else one.velocity.x
        val newOneYvel: Int = if (from.pos.y > one.pos.y) one.velocity.y + 1 else if (from.pos.y < one.pos.y) one.velocity.y - 1 else one.velocity.y
        val newOneZvel: Int = if (from.pos.z > one.pos.z) one.velocity.z + 1 else if (from.pos.z < one.pos.z) one.velocity.z - 1 else one.velocity.z
        return Pos3D(newOneXvel, newOneYvel, newOneZvel)
    }


    tailrec fun applyGravityToAll(applied: List<Moon>, todo: List<Moon>) : List<Moon> {
        val target = todo.first()
        val nextTodo = todo.minus(target)
        if (nextTodo.isEmpty()) return applied + target
        val others = applied + nextTodo
        val targetWithGravity = applyGravity(target, others)
        val othersWithGravity = others.map { applyGravity(it, others.minus(it).plus(target)) }
        val newApplied = applied.map { othersWithGravity[othersWithGravity.indexOf(it)] }.plus(targetWithGravity)
        val newTodo = nextTodo.map { othersWithGravity[othersWithGravity.indexOf(it)] }
        return applyGravityToAll(newApplied, newTodo)
    }

    tailrec fun dance(moons: List<Moon>, step: Int, stop: Int): List<Moon> {
        if (step == stop) return moons
        val afterGravity = applyGravityToAll(emptyList(), moons)//moons.map { applyGravity(it, moons.minus(it)) }
        val afterVelocity = afterGravity.map { it.copy(pos = it.pos.plus(it.velocity)) }
        return dance(afterVelocity, step + 1, stop)
    }

    tailrec fun untilRepeat(moons: List<Moon>, first: String, cycles: Long, hashFunc: (Moon) -> String) : Long {
        val hash = moons.map { hashFunc(it) }.joinToString("|")
        if (first == hash && cycles != 0L) return cycles
        val afterGravity = applyGravityToAll(emptyList(), moons)
        val afterVelocity = afterGravity.map { it.copy(pos = it.pos.plus(it.velocity)) }
        return untilRepeat(afterVelocity, first, cycles + 1, hashFunc)
    }

    fun solvePt1(input: List<String>, steps: Int) : Int {
        val moons = input.mapIndexed(::parseMoon)
        val afterDance = dance(moons, 0, steps)
        return afterDance.sumBy { it.total() }
    }

    fun solvePt2(input: List<String>) : Long {
        val moons = input.mapIndexed(::parseMoon)

        val hashFuncs = listOf<(Moon) -> String>(
            {m -> "${m.pos.x},${m.velocity.x}"},
            {m -> "${m.pos.y},${m.velocity.y}"},
            {m -> "${m.pos.z},${m.velocity.z}"})

        val firstRepeats = hashFuncs.map { hashFunc -> untilRepeat(moons, moons.map { hashFunc(it) }.joinToString("|"), 0, hashFunc) }
        return lcm(firstRepeats[0], firstRepeats[1], firstRepeats[2])
    }

}