package feature.article.service.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleFeedEntity(
    @SerialName("items") val articles: List<ArticleItemEntity> = listOf(),
) {
    @Serializable
    data class ArticleItemEntity(
        @SerialName("link") val url: String,
        @SerialName("title") val title: String,
        @SerialName("description") val description: String,
        @SerialName("pubDate") val date: String,
    )
}
