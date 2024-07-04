package feature.home.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResourceLanguageEntity(
    @SerialName("en") val en: String?,
    @SerialName("id") val id: String?,
)
