import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


fun readInput(name: String) = File("src", "$name.txt").readLines()

fun readInputAsString(name: String) = File("src", "$name.txt").readText()

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
