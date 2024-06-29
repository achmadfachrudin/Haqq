package feature.quran.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Verse(
    val id: Int, // verse/ayah id
    val chapterId: Int, // chapter number
    val verseNumber: Int,
    val verseKey: String,
    val hizbNumber: Int,
    val rubElHizbNumber: Int,
    val rukuNumber: Int,
    val manzilNumber: Int,
    val sajdahNumber: Int,
    val pageNumber: Int,
    val juzNumber: Int,
    val textArabic: String,
    val textTransliteration: String,
    val textTranslation: String,
)
