package feature.quran.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslationsEntity(
    @SerialName("translations") val translations: List<TranslationEntity>?,
    @SerialName("meta") val meta: MetaEntity?,
) {
    @Serializable
    data class TranslationEntity(
//        @SerialName("id") val id: Int?,
        @SerialName("resource_id") val resourceId: Int?,
        @SerialName("text") val text: String?,
    )

    @Serializable
    data class MetaEntity(
        @SerialName("translation_name") val translationName: String?,
        @SerialName("author_name") val authorName: String?,
    )
}
