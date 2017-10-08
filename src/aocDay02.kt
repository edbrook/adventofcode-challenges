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
    private val mCurrentCode = ArrayList<Char>(4)
    private var mCurrentRow: Int = 2
    private var mCurrentCol: Int = 0

    companion object {
        val UP = 0
        val DOWN = 1
        val LEFT = 2
        val RIGHT = 3

        val sCodePad = arrayOf(
                arrayOf(' ', ' ', '1', ' ', ' '),
                arrayOf(' ', '2', '3', '4', ' '),
                arrayOf('5', '6', '7', '8', '9'),
                arrayOf(' ', 'A', 'B', 'C', ' '),
                arrayOf(' ', ' ', 'D', ' ', ' '))
    }

    fun getCode(): String {
        return mCurrentCode.joinToString(separator = "")
    }

    fun getCurrentDigit(): Char {
        return sCodePad[mCurrentRow][mCurrentCol]
    }

    fun getDigitAt(col: Int, row: Int): Char {
        return sCodePad[row][col]
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
        var dR = mCurrentRow
        var dC = mCurrentCol

        when (direction) {
            UP -> dR--
            DOWN -> dR++
            LEFT -> dC--
            RIGHT -> dC++
        }

        val range = IntRange(0, 4)

        if (range.contains(dR) && range.contains(dC) && getDigitAt(dC, dR) != ' ') {
            mCurrentRow = dR
            mCurrentCol = dC
        }
    }
}
