package core.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CurrencyTransformation : VisualTransformation {
    companion object {
        const val MAX_DIGITS = 12
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmedText = text.text

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

        // Define an offset translator to handle cursor position correctly
        val offsetTranslator =
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                /*
                1 -> 0
                20 -> 0
                300 -> 0
                4.000 -> 1
                50.000 -> 1
                600.000 -> 1
                7.000.000 -> 2
                80.000.000 -> 2
                900.000.000 -> 2
            10  1.000.000.000 -> 3
            11  11.000.000.000 -> 3
            12  120.000.000.000 -> 3
                 */
                    return offset + (offset - 1) / 3
                }

                override fun transformedToOriginal(offset: Int): Int = offset - (offset - 1) / 4
            }

        return TransformedText(AnnotatedString(formattedText), offsetTranslator)
    }
}
