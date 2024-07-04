package feature.study.service.source.remote

import core.data.ApiResponse
import core.data.NetworkSource.Youtube
import core.data.safeRequest
import feature.study.service.entity.ChannelEntity
import feature.study.service.entity.KeyEntity
import feature.study.service.entity.YoutubeEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class StudyRemoteImp(
    private val supabaseHttpClient: HttpClient,
    private val youtubeHttpClient: HttpClient,
) : StudyRemote {
    override suspend fun fetchYoutubeAPI(): ApiResponse<List<KeyEntity>> =
        supabaseHttpClient.safeRequest {
            method = HttpMethod.Get
            url("youtube_api")
        }

    override suspend fun fetchChannels(): ApiResponse<List<ChannelEntity>> =
        supabaseHttpClient.safeRequest {
            method = HttpMethod.Get
            url("channel")
        }

    override suspend fun fetchLiveVideo(
        key: String,
        query: String,
    ): ApiResponse<YoutubeEntity> =
        youtubeHttpClient.safeRequest {
            method = HttpMethod.Get
            url("search")
            parameter(Youtube.PARAM_QUERY, query)
            parameter(Youtube.PARAM_KEY, key)
            parameter(Youtube.PARAM_MAX_RESULTS, Youtube.DEFAULT_MAX_RESULTS_SINGLE)
            parameter(Youtube.PARAM_PART, Youtube.DEFAULT_PART)
            parameter(Youtube.PARAM_TYPE, Youtube.DEFAULT_TYPE_LIVE)
            parameter(Youtube.PARAM_EVENT_TYPE, Youtube.DEFAULT_EVENT_TYPE_VIDEO)
        }

    override suspend fun fetchVideoList(
        key: String,
        channelId: String,
    ): ApiResponse<YoutubeEntity> =
        youtubeHttpClient.safeRequest {
            method = HttpMethod.Get
            url("search")
            parameter(Youtube.PARAM_KEY, key)
            parameter(Youtube.PARAM_CHANNEL_ID, channelId)
            parameter(Youtube.PARAM_MAX_RESULTS, Youtube.DEFAULT_MAX_RESULTS)
            parameter(Youtube.PARAM_PART, Youtube.DEFAULT_PART)
            parameter(Youtube.PARAM_TYPE, Youtube.DEFAULT_TYPE_VIDEO)
            parameter(Youtube.PARAM_ORDER, Youtube.DEFAULT_ORDER_DATE)
        }
}
