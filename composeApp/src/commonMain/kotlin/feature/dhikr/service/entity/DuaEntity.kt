package feature.dhikr.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DuaEntity(
    @SerialName("id") val id: Int?,
    @SerialName("title_id") val titleId: String?,
    @SerialName("title_en") val titleEn: String?,
    @SerialName("text_indopak") val textIndopak: String?,
    @SerialName("text_uthmani") val textUthmani: String?,
    @SerialName("text_transliteration") val textTransliteration: String?,
    @SerialName("text_translation_id") val textTranslationId: String?,
    @SerialName("text_translation_en") val textTranslationEn: String?,
    @SerialName("hadith_id") val hadithId: String?,
    @SerialName("hadith_en") val hadithEn: String?,
    @SerialName("tag") val tag: String?,
)
