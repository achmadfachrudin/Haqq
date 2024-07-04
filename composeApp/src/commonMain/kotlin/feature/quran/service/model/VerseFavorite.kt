package feature.quran.service.model

import kotlinx.serialization.Serializable

@Serializable
data class VerseFavorite(
    val verseId: Int,
    val verseNumber: Int,
    val chapterId: Int,
    val chapterNameSimple: String,
)
