package feature.article.service.source.remote

import core.data.ApiResponse
import feature.article.service.entity.ArticleFeedEntity
import feature.article.service.entity.ArticleMediaEntity

interface ArticleRemote {
    suspend fun fetchMedias(): ApiResponse<List<ArticleMediaEntity>>

    suspend fun fetchArticles(feedUrl: String): ApiResponse<ArticleFeedEntity>
}
