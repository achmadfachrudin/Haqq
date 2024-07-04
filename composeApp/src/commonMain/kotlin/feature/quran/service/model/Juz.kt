package feature.quran.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Juz(
    val id: Int,
    val juzNumber: Int,
    val chapterIds: List<Int>,
    val firstChapterNumber: Int,
    val firstVerseNumber: Int,
    val firstVerseId: Int,
    val lastChapterNumber: Int,
    val lastVerseNumber: Int,
    val lastVerseId: Int,
    val versesCount: Int,
)
