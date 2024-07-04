package feature.study.service.model

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val id: String,
    val title: String,
    val publishedAt: String,
    val thumbnailUrl: String,
    val isLive: Boolean,
)
