package core.util

fun String?.toLongDefault(default: Long = 0): Long = this?.toLongOrNull() ?: default

fun String?.toDoubleDefault(default: Double = 0.0): Double = this?.toDoubleOrNull() ?: default

fun String.searchBy(query: String): Boolean {
    val original =
        this
            .lowercase()
            .filter { it.isLetterOrDigit() }

    val search =
        query
            .lowercase()
            .filter { it.isLetterOrDigit() }

    return original.contains(search)
}

fun String.findRangeIndex(focusString: String): IntRange? {
    val startIndex = indexOf(focusString)
    return if (startIndex != -1) {
        startIndex..<startIndex + focusString.length
    } else {
        null
    }
}
