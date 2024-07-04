package feature.study.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelEntity(
    @SerialName("id") val id: String?,
    @SerialName("name") val name: String?,
    @SerialName("image") val image: String?,
    @SerialName("url") val url: String?,
)
