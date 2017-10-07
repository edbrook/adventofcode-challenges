import java.io.File
import java.nio.file.Paths

/**
 * Created by edbrook on 07/10/2017.
 */
fun main(args: Array<String>) {
    val path = Paths.get("").toAbsolutePath().toString()

    val directions = File("$path/input.txt")
            .inputStream()
            .bufferedReader()
            .use { it.readLine().split(", ") }

    val tc = TaxiCab(DEBUG = true)
    for (move in directions) {
        tc.processMove(move)
    }
    val dist = tc.getDistanceFromStart()
    val dir = TaxiCab.directionToName(tc.getFinalDirection())

    println("Distance from start: $dist")
    println("Final direction: $dir")
}


class TaxiCab(private val DEBUG: Boolean = false) {
    companion object {
        val NORTH = 0
        val EAST = 1
        val SOUTH = 2
        val WEST = 3

        private data class Move(val rot: String, val by: Int)

        fun directionToName(dir: Int): String {
            return when (dir) {
                NORTH -> "North"
                EAST -> "East"
                SOUTH -> "South"
                WEST -> "West"
                else -> throw IllegalArgumentException("Invalid direction: $dir")
            }
        }
    }

    private var dxNS = 0
    private var dxEW = 0
    private var curDir: Int = NORTH

    fun processMove(cmd: String) {
        val move = extractRotBy(cmd)
        curDir = nextDirection(move.rot, curDir)
        updateVecDistance(curDir, move.by)
        if (DEBUG) println("${move.rot} -> ${move.by} " +
                "(current direction: $curDir, " +
                "dxNS: $dxNS, " +
                "dxEW: $dxEW)")
    }

    fun getDistanceFromStart(): Int {
        return Math.abs(dxNS) + Math.abs(dxEW)
    }

    fun getFinalDirection(): Int {
        return curDir
    }

    private fun extractRotBy(move: String): Move {
        val rot = move.substring(0, 1)
        val by = move.substring(1).toInt(10)
        return Move(rot, by)
    }

    private fun nextDirection(rot: String, curDir: Int): Int {
        var nextDir = curDir
        return when (rot) {
            "R" -> if (++nextDir > WEST) NORTH else nextDir
            "L" -> if (--nextDir < NORTH) WEST else nextDir
            else -> throw IllegalArgumentException("Invalid rotation")
        }
    }

    private fun updateVecDistance(dir: Int, dist: Int) {
        when (dir) {
            NORTH -> dxNS += dist
            SOUTH -> dxNS -= dist
            EAST  -> dxEW += dist
            WEST  -> dxEW -= dist
        }
    }
}