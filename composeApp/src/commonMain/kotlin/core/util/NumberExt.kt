package core.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun Int?.orZero(): Int = this ?: 0

fun Long?.orZero(): Long = this ?: 0

fun Int?.isNullOrZero(): Boolean = this == null || this == 0

fun Int.isNotZero(): Boolean = this > 0

fun Int.formatAsCurrency(showPrefix: Boolean = true): String {
    val prefix = if (showPrefix) "Rp" else ""
    val trimmedText = this.toString()

    // Reverse the string to start grouping from the end
    val reversed = trimmedText.reversed()

    // Group the characters with hyphens from the end
    var result = ""
    for (i in reversed.indices) {
        result += reversed[i]
        if (i % 3 == 2 && i != reversed.length - 1) result += "."
    }

    // Reverse the string back to the original order with the new formatting
    val formattedText = result.reversed()
    return "$prefix$formattedText"
}

fun Long.formatAsCurrency(showPrefix: Boolean = true): String {
    val prefix = if (showPrefix) "Rp" else ""
    val trimmedText = this.toString()

    // Reverse the string to start grouping from the end
    val reversed = trimmedText.reversed()

    // Group the characters with hyphens from the end
    var result = ""
    for (i in reversed.indices) {
        result += reversed[i]
        if (i % 3 == 2 && i != reversed.length - 1) result += "."
    }

    // Reverse the string back to the original order with the new formatting
    val formattedText = result.reversed()
    return "$prefix$formattedText"
}

// milliseconds: Long
@OptIn(ExperimentalTime::class)
fun Long.toLocalDateTime(): LocalDateTime =
    Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())

fun Int.addLeadingZero(digits: Int = 2): String {
    val numString = this.toString()
    val zerosToAdd = if (numString.length < digits) digits - numString.length else 0
    return "0".repeat(zerosToAdd) + numString
}
