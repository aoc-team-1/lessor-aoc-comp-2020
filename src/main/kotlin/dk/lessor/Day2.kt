package dk.lessor

fun main() {
    val lines = readFile("day_2.txt")
    val validOldPasswords = checkOldPassword(lines)
    println(validOldPasswords.size)

    val validNewPasswords = checkNewPassword(lines)
    println(validNewPasswords.size)
}

fun checkOldPassword(lines: List<String>): List<String> {
    val result = mutableListOf<String>()

    for (line in lines) {
        val (policy, password) = line.split(": ")
        val (amounts, letter) = policy.split(" ")
        val (min, max) = amounts.split("-").map { it.toInt() }

        val count = password.sumBy { if (it == letter.first()) 1 else 0 }

        if (count in min..max) {
            result.add(line)
        }
    }

    return result
}


fun checkNewPassword(lines: List<String>): List<String> {
    val result = mutableListOf<String>()

    for (line in lines) {
        val (policy, password) = line.split(": ")
        val (amounts, letter) = policy.split(" ")
        val (pos1, pos2) = amounts.split("-").map { it.toInt() }

        if ((password[pos1-1] == letter.first()) xor (password[pos2-1] == letter.first())) {
            result.add(line)
        }
    }

    return result
}