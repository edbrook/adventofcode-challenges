import java.io.File
import java.nio.file.Paths

fun main(array: Array<String>) {
    var tls = 0
    var nonTls = 0
    val root = Paths.get("").toAbsolutePath().toString()
    File("$root/input_day07.txt")
            .inputStream()
            .bufferedReader()
            .lines()
            .forEach { addr ->
                if (supportsTLS(addr)) {
                    tls++
                } else {
                    nonTls++
                }
            }
    println("Supports TLS: $tls")
    println("Non TLS: $nonTls")
}

fun supportsTLS(addr: String): Boolean {
    val matchAbba = Regex(".*?(.)(?!\\1)(.)\\2\\1.*?")
    val tokens = tokenizer(addr)
    var hnAbba = false
    var snAbba = false
    tokens.forEach { token ->
        if (matchAbba.matches(token.value)) {
            if (token.isHyperNet) {
                hnAbba = true
            } else {
                snAbba = true
            }
        }
    }
    return snAbba && !hnAbba
}

data class Token (val value: String, val isHyperNet: Boolean)

fun tokenizer(line: String): Array<Token> {
    val tokens = ArrayList<Token>()
    var token = StringBuilder()
    line.forEach { c ->
        var clear = false
        if (token.isNotEmpty()) {
            if (c == '[') {
                tokens.add(Token(token.toString(), false))
                clear = true
            } else if (c == ']') {
                tokens.add(Token(token.toString(), true))
                clear = true
            }
        }
        if (clear) {
            token = StringBuilder()
        }
        if (!clear) {
            token.append(c)
        }
    }
    if (token.isNotEmpty()) {
        tokens.add(Token(token.toString(), false))
    }
    return tokens.toTypedArray()
}