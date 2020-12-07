package dk.lessor

fun main() {
    val lines = readFile("day_7.txt")
    val bags = bagParser(lines)
    val canHaveShiny = containsBag(bags)
    println(canHaveShiny.size)
    println(countBags(bags))
}

fun containsBag(bags: Map<String, List<Bag>>, name: String = "shiny gold"): List<String> {
    val result = mutableListOf<String>()
    result += bags.filter { it.value.any { b -> b.name == name } }.keys

    return (result + result.flatMap { containsBag(bags, it) }).distinct()
}

fun countBags(bags: Map<String, List<Bag>>, name: String = "shiny gold"): Int {
    val contains = bags[name] ?: return 0

    return contains.sumBy(Bag::count) + contains.sumBy { it.count * countBags(bags, it.name) }
}

fun bagParser(lines: List<String>): Map<String, List<Bag>> {
    val result = mutableMapOf<String, List<Bag>>()

    for (line in lines) {
        val (main, inside) = line.split(" contain ")
        val key = main.replace(" bag[s]?\\.?".toRegex(), "").trim()

        if (inside == "no other bags.") {
            result[key] = emptyList()
            continue
        }

        val contains = inside.split(", ").map { b ->
            val count = b.take(1).toInt()
            val name = b.drop(2).replace(" bag[s]?\\.?".toRegex(), "").trim()
            Bag(name, count)
        }

        result[key] = contains
    }

    return result
}

data class Bag(val name: String, val count: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bag

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}