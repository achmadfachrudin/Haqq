package feature.charity.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharityEntity(
    @SerialName("created_at") val createdAt: String?,
    @SerialName("image_url") val imageUrl: String?,
)
