package feature.article.service

import core.data.ApiResponse
import core.data.DataState
import feature.article.service.mapper.mapToArticleItem
import feature.article.service.model.ArticleMedia
import feature.article.service.source.remote.ArticleRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onStart

class ArticleRepository(
    private val remote: ArticleRemote,
) {
    fun fetchMedias() =
        flow {
            when (val result = remote.fetchMedias()) {
                is ApiResponse.Error -> emit(DataState.Error(result.message))
                is ApiResponse.Success -> {
                    val remoteResult =
                        result.body.map {
                            val articles = fetchArticles(it.feedUrl.orEmpty()).last()

                            ArticleMedia(
                                name = it.name.orEmpty(),
                                feedUrl = it.feedUrl.orEmpty(),
                                articles = articles,
                            )
                        }
                    emit(DataState.Success(remoteResult))
                }
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    private fun fetchArticles(feedUrl: String) =
        flow {
            when (val result = remote.fetchArticles(feedUrl)) {
                is ApiResponse.Error -> emit(listOf())
                is ApiResponse.Success -> emit(result.body.articles.map { it.mapToArticleItem() })
            }
        }.flowOn(Dispatchers.IO)
}
