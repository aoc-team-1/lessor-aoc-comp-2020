package dk.lessor

import java.nio.file.Paths

fun readFile(fileName: String): List<String> {
    val file = Paths.get(ClassLoader.getSystemResource(fileName).toURI()).toFile()
    return file.readLines()
}