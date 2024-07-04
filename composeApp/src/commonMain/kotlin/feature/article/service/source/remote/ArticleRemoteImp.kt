package feature.article.service.source.remote

import core.data.ApiResponse
import core.data.NetworkSource.Article
import core.data.safeRequest
import feature.article.service.entity.ArticleFeedEntity
import feature.article.service.entity.ArticleMediaEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class ArticleRemoteImp(
    private val articleHttpClient: HttpClient,
    private val supabaseHttpClient: HttpClient,
) : ArticleRemote {
    override suspend fun fetchMedias(): ApiResponse<List<ArticleMediaEntity>> =
        supabaseHttpClient.safeRequest {
            method = HttpMethod.Get
            url("article_media")
        }

    override suspend fun fetchArticles(feedUrl: String): ApiResponse<ArticleFeedEntity> =
        articleHttpClient.safeRequest {
            method = HttpMethod.Get
            url("api.json")
            parameter(Article.PARAM_RSS, feedUrl)
        }
}
