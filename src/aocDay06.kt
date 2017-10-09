import java.io.File
import java.nio.file.Paths


fun main(args: Array<String>) {
    val root = Paths.get("").toAbsolutePath().toString()

    val code = RepetitionCode()
    File("$root/input_day06.txt")
            .inputStream()
            .bufferedReader()
            .forEachLine {
                code.update(it)
            }
    println("Decoded Message: ${code.getMessage()}")
}

class RepetitionCode {
    private val data = ArrayList<HashMap<Char, Int>>()

    fun update(line: String) {
        if (data.size == 0) {
            for (i in 0..line.lastIndex) {
                data.add(HashMap())
            }
        }
        if (line.length != data.size) {
            throw IllegalArgumentException("Invalid message length")
        }
        line.forEachIndexed {
            index, chr ->
                val n = data[index].getOrDefault(chr, 0)
                data[index][chr] = n + 1
        }
    }

    fun getMessage(): String {
        val message = Array(data.size, { ' ' })
        data.forEachIndexed {
            index, letterCounts ->
                var count = -1
                var curChar = ' '
                letterCounts.forEach {
                    if (count == -1 || it.value < count) {
                        curChar = it.key
                        count = it.value
                    }
                }
                message[index] = curChar
        }
        return message.joinToString("")
    }
}