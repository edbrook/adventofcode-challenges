import java.io.File
import java.nio.file.Paths

/**
 * Created by edbrook on 07/10/2017.
 */
fun main(args: Array<String>) {
    val path = Paths.get("").toAbsolutePath().toString()

    val moves = File("$path/input_day01.txt")
            .inputStream()
            .bufferedReader()
            .use { it.readLine().split(", ") }

    val tc = TaxiCab()
    var dist = 0

    for (move in moves) {
        val crossed = tc.processMove(move)
        if (crossed.size > 0) {
            dist = tc.getDistanceFromStart(crossed[0])
            println("Crossed at: ${crossed[0]}")
            break
        }
    }

    println("Distance to first crossed path from start: $dist")
}


class TaxiCab(private val DEBUG: Boolean = false) {
    companion object {
        val NORTH = 0
        val EAST  = 1
        val SOUTH = 2
        val WEST  = 3

        private data class Move(val rot: String, val by: Int)
        data class MapLocation(val ew: Int, val ns: Int) {
            override fun toString(): String {
                return "($ew, $ns)"
            }
        }

        fun directionToName(dir: Int): String {
            return when (dir) {
                NORTH -> "North"
                EAST  -> "East"
                SOUTH -> "South"
                WEST  -> "West"
                else  -> throw IllegalArgumentException("Invalid direction: $dir")
            }
        }
    }

    private var curDir: Int = NORTH
    private var lastLoc = MapLocation(0, 0)
    private var curLoc = MapLocation(0, 0)
    private var visited = HashSet<MapLocation>()

    fun processMove(cmd: String): ArrayList<MapLocation> {
        val move = extractRotBy(cmd)
        curDir = nextDirection(move.rot, curDir)

        val nextLoc = nextLocation(curDir, move.by, curLoc)
        val crossed = updateSeen(curLoc, nextLoc)

        if (DEBUG) crossed.forEach { println("  crossed: $it") }

        if (DEBUG) println("${move.rot} -> ${move.by} " +
                "(current direction: $curDir, " +
                "location: (${nextLoc.ew}, ${nextLoc.ns}))")

        lastLoc = curLoc
        curLoc = nextLoc

        return crossed
    }

    fun getDistanceFromStart(loc: MapLocation = curLoc): Int {
        return Math.abs(loc.ns) + Math.abs(loc.ew)
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

    private fun nextLocation(dir: Int, dist: Int, loc: MapLocation): MapLocation {
        return when (dir) {
            NORTH -> MapLocation(loc.ew, loc.ns + dist)
            SOUTH -> MapLocation(loc.ew, loc.ns - dist)
            EAST  -> MapLocation(loc.ew + dist, loc.ns)
            WEST  -> MapLocation(loc.ew - dist, loc.ns)
            else  -> throw IllegalArgumentException("Invalid rotation")
        }
    }

    private fun updateSeen(cur: MapLocation, next: MapLocation): ArrayList<MapLocation> {
        val crossed = ArrayList<MapLocation>()
        var loc: MapLocation

        if (DEBUG) println("move from: $cur -> $next")

        var range = when (cur.ew < next.ew) {
            true -> cur.ew until next.ew
            false -> cur.ew downTo next.ew + 1
        }

        for (x in range) {
            loc = MapLocation(x, cur.ns)
            if (visited.contains(loc)) {
                crossed.add(loc)
            } else {
                visited.add(loc)
            }
        }

        range = when (cur.ns < next.ns) {
            true -> cur.ns until next.ns
            false -> cur.ns downTo next.ns + 1
        }

        for (y in range) {
            loc = MapLocation(cur.ew, y)
            if (visited.contains(loc)) {
                crossed.add(loc)
            } else {
                visited.add(loc)
            }
        }
        return crossed
    }
}