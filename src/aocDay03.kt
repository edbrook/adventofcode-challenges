import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val root = Paths.get("")
            .toAbsolutePath()
            .toString()

    var possible = 0

    File("$root/input_day03.txt")
            .inputStream()
            .bufferedReader()
            .forEachLine {
                val (a, b, c) = parseLine(it)
                if (isPossibleTriangle(a, b, c)) {
                    possible++
                }
            }

    println("Possible: $possible")
}

fun parseLine(line: String): IntArray {
    return line.trim()
            .split(Regex("\\s+"))
            .map { it.toInt() }
            .sorted()
            .toIntArray()
}

fun isPossibleTriangle(a: Int, b: Int, c: Int): Boolean {
    return a + b > c
}