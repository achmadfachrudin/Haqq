package feature.study.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YoutubeEntity(
    @SerialName("items")
    val items: List<VideoItemEntity>?,
) {
    @Serializable
    data class VideoItemEntity(
        val id: Id?,
        val snippet: Snippet?,
    ) {
        @Serializable
        data class Id(
            val videoId: String?,
        )

        @Serializable
        data class Snippet(
            val title: String?,
            val thumbnails: Thumbnails?,
            val publishedAt: String?,
            val liveBroadcastContent: String?,
        ) {
            @Serializable
            data class Thumbnails(
                val medium: Medium?,
            ) {
                @Serializable
                data class Medium(
                    val url: String?,
                )
            }
        }
    }
}
