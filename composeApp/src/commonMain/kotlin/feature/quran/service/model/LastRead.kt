package feature.quran.service.model

import kotlinx.serialization.Serializable

@Serializable
data class LastRead(
    val verseId: Int,
    val verseNumber: Int,
    val chapterId: Int,
    val chapterNameSimple: String,
    val progressFloat: Float,
    val progressInt: Int,
)
