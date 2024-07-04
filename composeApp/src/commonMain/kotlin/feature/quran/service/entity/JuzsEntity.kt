package feature.quran.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JuzsEntity(
    @SerialName("juzs") val juzs: List<JuzEntity>?,
) {
    @Serializable
    data class JuzEntity(
        @SerialName("id") val id: Int?,
        @SerialName("juz_number") val juzNumber: Int?,
        @SerialName("verse_mapping") val verseMapping: Map<String, String>?,
        @SerialName("first_verse_id") val firstVerseId: Int?,
        @SerialName("last_verse_id") val lastVerseId: Int?,
        @SerialName("verses_count") val versesCount: Int?,
    )
}
