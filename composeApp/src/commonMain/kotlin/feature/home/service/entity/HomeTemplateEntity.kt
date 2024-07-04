package feature.home.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeTemplateEntity(
    @SerialName("position") val position: Int?,
    @SerialName("type") val type: String?,
    @SerialName("label") val label: ResourceLanguageEntity?,
    @SerialName("text") val text: ResourceLanguageEntity?,
    @SerialName("images") val images: String?,
    @SerialName("links") val links: String?,
)
