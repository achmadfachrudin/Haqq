package feature.study.service.source.remote

import core.data.ApiResponse
import feature.study.service.entity.ChannelEntity
import feature.study.service.entity.KeyEntity
import feature.study.service.entity.YoutubeEntity

interface StudyRemote {
    suspend fun fetchYoutubeAPI(): ApiResponse<List<KeyEntity>>

    suspend fun fetchChannels(): ApiResponse<List<ChannelEntity>>

    suspend fun fetchLiveVideo(
        key: String,
        query: String,
    ): ApiResponse<YoutubeEntity>

    suspend fun fetchVideoList(
        key: String,
        channelId: String,
    ): ApiResponse<YoutubeEntity>
}
