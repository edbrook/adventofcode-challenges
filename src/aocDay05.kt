import java.io.File
import java.nio.file.Paths
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

fun main(args: Array<String>) {
    val root = Paths.get("").toAbsolutePath().toString()

    File("$root/input_Day05.txt")
            .inputStream()
            .bufferedReader()
            .use {
                println(DoorPassword.decode(it.readLine()))
            }
}

object DoorPassword {
    private val md5 = MessageDigest.getInstance("MD5")

    fun decode(doorId: String): String {
        var id = 0
        var done = 0
        val arrRange = IntRange(0, 7)
        val passwd = Array(8, { ' ' })
        while (done < 8) {
            val nextHash = hash("$doorId$id")
            if (nextHash.substring(0, 5) == "00000") {
                val pos = nextHash[5].toInt() - 48
                val chr = nextHash[6]
                if (arrRange.contains(pos) && passwd[pos] == ' ') {
                    passwd[pos] = chr
                    done++
                }
            }
            id++
        }
        return passwd.joinToString("").toLowerCase()
    }

    private fun hash(input: String): String {
        return DatatypeConverter.printHexBinary(
                md5.digest(input.toByteArray()))
    }
}