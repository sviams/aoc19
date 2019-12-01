import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.api.dsl.TestContainer
import java.time.Duration
import kotlin.system.measureNanoTime

fun TestContainer.benchedIt(desc: String, iterations: Int, body: () -> Unit) {
    test("BENCH it $desc - $iterations iterations") {
        val dur = Duration.ofNanos((0 until iterations).map { measureNanoTime(body) }.average().toLong())
        println("Benchmark average over $iterations iterations:  ${dur.toMinutes()}m ${dur.seconds}s ${dur.nano / 1000000}ms")
    }
}

fun TestBody.readLine(name: String) : String = this::class.java.classLoader.getResource(name).readText()

fun TestBody.readLines(name: String) : List<String> = this::class.java.classLoader.getResource(name).readText().toLines()

fun String.toLines(): List<String> = this.split("\n")

fun TestContainer.readLine(name: String) : String = this::class.java.classLoader.getResource(name).readText()

fun TestContainer.readLines(name: String) : List<String> = this::class.java.classLoader.getResource(name).readText().toLines()
