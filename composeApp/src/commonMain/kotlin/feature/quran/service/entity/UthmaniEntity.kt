package feature.quran.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UthmaniEntity(
    @SerialName("verses") val verses: List<VerseEntity>?,
) {
    @Serializable
    data class VerseEntity(
        @SerialName("id") val id: Int?,
        @SerialName("verse_key") val verseKey: String?,
        @SerialName("text_uthmani") val textUthmani: String?,
    )
}
