package feature.study.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    val id: String,
    val name: String,
    val image: String,
    val url: String,
)
