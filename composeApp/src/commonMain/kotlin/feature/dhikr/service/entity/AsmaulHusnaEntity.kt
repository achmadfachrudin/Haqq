package feature.dhikr.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AsmaulHusnaEntity(
    @SerialName("id") val id: Int?,
    @SerialName("text_indopak") val textIndopak: String?,
    @SerialName("text_uthmani") val textUthmani: String?,
    @SerialName("text_transliteration") val textTransliteration: String?,
    @SerialName("text_translation_id") val textTranslationId: String?,
    @SerialName("text_translation_en") val textTranslationEn: String?,
)
