package feature.quran.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageEntity(
    @SerialName("id") val id: Int?,
    @SerialName("page_number") val pageNumber: Int?,
    @SerialName("chapters") val chapters: String?,
    @SerialName("first_chapter_number") val firstChapterNumber: Int?,
    @SerialName("first_verse_number") val firstVerseNumber: Int?,
    @SerialName("first_verse_id") val firstVerseId: Int?,
    @SerialName("last_chapter_number") val lastChapterNumber: Int?,
    @SerialName("last_verse_number") val lastVerseNumber: Int?,
    @SerialName("last_verse_id") val lastVerseId: Int?,
    @SerialName("verses_count") val versesCount: Int?,
)
