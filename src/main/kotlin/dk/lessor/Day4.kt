package dk.lessor


fun main() {
    val lines = readFile("day_4.txt").toMutableList()
    lines.add("")

    val passports = parser(lines)
    println(passports.filter { it.simpleValid() }.size)
    println(passports.filter { it.valid() }.size)


}

fun parser(lines: List<String>): List<Passport> {
    val passports = mutableListOf<Passport>()

    val current = StringBuilder()
    for (line in lines) {
        if (line.isNotBlank()) {
            current.append(" $line")
            continue
        }

        // parse
        val keyValues = current.toString().trim().split(" ").associate {
            val (key, value) = it.split(":")
            key to value
        }

        passports.add(
            Passport(
                keyValues["byr"],
                keyValues["iyr"],
                keyValues["eyr"],
                keyValues["hgt"],
                keyValues["hcl"],
                keyValues["ecl"],
                keyValues["pid"],
                keyValues["cid"],
            )
        )

        current.clear()
    }

    return passports
}

data class Passport(
    val byr: String?,
    val iyr: String?,
    val eyr: String?,
    val hgt: String?,
    val hcl: String?,
    val ecl: String?,
    val pid: String?,
    val cid: String?,
) {
    fun simpleValid(): Boolean {
        return byr != null
                && iyr != null
                && eyr != null
                && hgt != null
                && hcl != null
                && ecl != null
                && pid != null
    }

    fun valid(): Boolean {
        return validByr()
                && validIyr()
                && validEyr()
                && validHgt()
                && validHcl()
                && validEcl()
                && validPid()
    }

    private fun validByr(): Boolean {
        val year = byr?.toInt() ?: return false
        if (year < 1920 || year > 2002) return false
        return true
    }

    private fun validIyr(): Boolean {
        val year = iyr?.toInt() ?: return false
        if (year < 2010 || year > 2020) return false
        return true
    }

    private fun validEyr(): Boolean {
        val year = eyr?.toInt() ?: return false
        if (year < 2020 || year > 2030) return false
        return true
    }

    private fun validHgt(): Boolean {
        val height = hgt?.dropLast(2)?.toInt() ?: return false
        val type = hgt.takeLast(2)

        if (type == "cm" && height in (150..193)) return true
        else if (type == "in" && height in (59..76)) return true
        return false
    }

    private fun validHcl(): Boolean {
        return hcl?.matches("#[a-z0-9]{6}".toRegex()) ?: false
    }

    private fun validEcl(): Boolean {
        return ecl?.let {
            listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(it)
        } ?: false
    }

    private fun validPid(): Boolean {
        return pid?.matches("[0-9]{9}".toRegex()) ?: false
    }
}