package feature.study.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KeyEntity(
    @SerialName("api") val api: String?,
)
