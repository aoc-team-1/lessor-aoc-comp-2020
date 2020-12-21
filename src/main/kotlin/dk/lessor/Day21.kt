package dk.lessor

fun main() {
    val lines = readFile("day_21.txt").map {
        val (ingredients, allergies) = it.split(" (contains ")
        ingredients.split(" ") to allergies.dropLast(1).split(", ")
    }

    val translations = identifyAllergyTranslations(lines)
    val count = lines.flatMap { it.first }.filter { !translations.containsKey(it) }.count()
    println(count)
    val dangerousIngredientsList = translations.toList().sortedBy { (_, value) -> value }.joinToString(",") { it.first }
    println(dangerousIngredientsList)
}

fun identifyAllergyTranslations(menu: List<Pair<List<String>, List<String>>>): Map<String, String> {
    val result = mutableMapOf<String, String>()
    val words = menu.flatMap { it.second }.distinct()

    while (result.size < words.size) {
        for (allergy in words) {
            val possible = menu.filter { it.second.contains(allergy) }.map { it.first }
            var word = possible.first().toSet()
            possible.forEach { word = word.intersect(it) }
            word = word.filter { !result.containsKey(it) }.toSet()
            if (word.size == 1) {
                result[word.first()] = allergy
            }
        }
    }

    return result
}

