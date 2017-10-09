import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val root = Paths.get("").toAbsolutePath().toString()

    var validCount = 0
    var sumRoomId = 0
    File("$root/input_day04.txt")
            .inputStream()
            .bufferedReader()
            .lines()
            .forEach {
                val room = Room.fromEncryptedString(it)
                if (RoomValidator.validate(room)) {
                    validCount++
                    sumRoomId += room.id
                }
            }
    println("\nTotal Valid: $validCount")
    println("Sum of Room Ids: $sumRoomId")
}

class Room private constructor(val id: Int, val name: String, val csum: String) {
    companion object {
        fun fromEncryptedString(name: String): Room {
            val idStart = name.lastIndexOf('-') + 1
            val csStart = name.indexOf('[', idStart) + 1
            val room = name.substring(0, idStart-1).replace("-", "")
            val id = name.substring(idStart, csStart-1).toInt()
            val csum = name.substring(csStart, name.lastIndex)
            return Room(id, room, csum)
        }
    }
}

object RoomValidator {
    fun validate(room: Room): Boolean {
        val letterCounts = countLetters(room.name)
        val letters = getSortedLetters(letterCounts).sliceArray(IntRange(0, 4))
        val mySum = letters.joinToString("")
        return mySum == room.csum
    }

    private fun countLetters(room: String): HashMap<Char, Int> {
        val letterCounts = HashMap<Char, Int>()
        room.forEach {
            val count = letterCounts.getOrDefault(it, 0)
            letterCounts[it] = count + 1
        }
        return letterCounts
    }

    private fun getSortedLetters(letterCounts: HashMap<Char, Int>): Array<Char> {
        return letterCounts.keys
                .sortedWith(compareBy({ -1 * letterCounts[it]!! }, { it } ))
                .toTypedArray()
    }
}