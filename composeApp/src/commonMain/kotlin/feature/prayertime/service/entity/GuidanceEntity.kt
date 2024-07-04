package feature.prayertime.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuidanceEntity(
    @SerialName("position") val position: Int?,
    @SerialName("type") val type: String?,
    @SerialName("title_id") val titleId: String?,
    @SerialName("title_en") val titleEn: String?,
    @SerialName("desc_id") val descId: String?,
    @SerialName("desc_en") val descEn: String?,
    @SerialName("image") val image: String?,
    @SerialName("text_indopak") val textIndopak: String?,
    @SerialName("text_uthmani") val textUthmani: String?,
    @SerialName("text_transliteration") val textTransliteration: String?,
    @SerialName("text_translation_id") val textTranslationId: String?,
    @SerialName("text_translation_en") val textTranslationEn: String?,
    @SerialName("hadith_id") val hadithId: String?,
    @SerialName("hadith_en") val hadithEn: String?,
)
