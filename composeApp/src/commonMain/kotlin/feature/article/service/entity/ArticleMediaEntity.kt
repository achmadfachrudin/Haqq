package feature.article.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleMediaEntity(
    @SerialName("name") val name: String?,
    @SerialName("feed_url") val feedUrl: String?,
)
