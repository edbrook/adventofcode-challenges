import java.io.File
import java.nio.file.Paths

/**
 * Created by edbrook on 07/10/2017.
 */

fun main (args: Array<String>) {
    val root = Paths.get("").toAbsolutePath().toString();

    val lock = SecurityLock()

    File("$root/input_day02.txt")
            .inputStream()
            .bufferedReader()
            .forEachLine {
                lock.processLine(it)
            }

    val code = lock.getCode()

    println("Code for lock: $code")
}


class SecurityLock {
    private val mCurrentCode = ArrayList<Int>(4)
    private var mCurrentRow: Int = 1
    private var mCurrentCol: Int = 1

    companion object {
        val UP = 0
        val DOWN = 1
        val LEFT = 2
        val RIGHT = 3

        val sCodePad = arrayOf(
                intArrayOf(1,2,3),
                intArrayOf(4,5,6),
                intArrayOf(7,8,9))
    }

    fun getCode(): String {
        return mCurrentCode.joinToString(separator = "")
    }

    fun getCurrentDigit(): Int {
        return sCodePad[mCurrentRow][mCurrentCol]
    }

    fun saveDigit() {
        mCurrentCode.add(getCurrentDigit())
    }

    fun processLine(line: String) {
        line.forEach {
            processMove(it)
        }
        saveDigit()
    }

    fun processMove(move: Char) {
        when (move) {
            'U' -> doMove(UP)
            'D' -> doMove(DOWN)
            'L' -> doMove(LEFT)
            'R' -> doMove(RIGHT)
        }
    }

    private fun doMove(direction: Int) {
        when (direction) {
            UP -> { if (--mCurrentRow < 0) mCurrentRow = 0 }
            DOWN -> { if (++mCurrentRow > 2) mCurrentRow = 2 }
            LEFT -> { if (--mCurrentCol < 0) mCurrentCol = 0 }
            RIGHT -> { if (++mCurrentCol > 2) mCurrentCol = 2 }
        }
    }
}
