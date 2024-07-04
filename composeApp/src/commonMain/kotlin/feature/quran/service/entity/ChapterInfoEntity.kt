package feature.quran.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChapterInfoEntity(
    @SerialName("chapter_info") val chapterInfo: InfoEntity?,
) {
    @Serializable
    data class InfoEntity(
        @SerialName("id") val id: Int?,
        @SerialName("chapter_id") val chapterId: Int?,
        @SerialName("language_name") val languageName: String?,
        @SerialName("short_text") val shortText: String?,
        @SerialName("source") val source: String?,
        @SerialName("text") val text: String?,
    )
}
