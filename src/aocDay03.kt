import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val root = Paths.get("")
            .toAbsolutePath()
            .toString()

    val possible = countValidTriangles("$root/input_day03.txt")

    println("Possible: $possible")
}

fun countValidTriangles(filename: String): Int {
    var possible = 0

    val triangles = arrayOf(
            IntArray(3),
            IntArray(3),
            IntArray(3))

    var side = 0
    File(filename)
            .inputStream()
            .bufferedReader()
            .forEachLine {
                side++
                side %= 3
                parseLine(it).forEachIndexed {
                    triangle, sideLength -> triangles[triangle][side] = sideLength
                }
                if (side == 0) {
                    possible += processTriangles(triangles)
                }
            }

    return possible
}

fun processTriangles(triangles: Array<IntArray>): Int {
    var possible = 0
    triangles.forEach {
        if (isPossibleTriangle(it)) {
            possible++
        }
    }
    return possible
}

fun parseLine(line: String): IntArray {
    return line.trim()
            .split(Regex("\\s+"))
            .map { it.toInt() }
            .toIntArray()
}

fun isPossibleTriangle(triangle: IntArray): Boolean {
    val (x, y, z) = triangle.sorted()
    return x + y > z
}