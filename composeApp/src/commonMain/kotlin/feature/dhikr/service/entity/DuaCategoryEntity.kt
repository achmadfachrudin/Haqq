package feature.dhikr.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DuaCategoryEntity(
    @SerialName("tag") val tag: String?,
    @SerialName("title_id") val titleId: String?,
    @SerialName("title_en") val titleEn: String?,
)
