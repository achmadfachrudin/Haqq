package feature.study.service.mapper

import core.util.DateUtil
import feature.study.service.entity.ChannelEntity
import feature.study.service.entity.YoutubeEntity
import feature.study.service.model.Channel
import feature.study.service.model.Video

internal fun ChannelEntity.mapToModel(): Channel =
    Channel(
        id = id.orEmpty(),
        name = name.orEmpty(),
        image = image.orEmpty(),
        url = url.orEmpty(),
    )

internal fun YoutubeEntity.mapToVideos(): List<Video> {
    val videos =
        items?.map {
            Video(
                id = it.id?.videoId.orEmpty(),
                title = it.snippet?.title.orEmpty(),
                thumbnailUrl =
                    it.snippet
                        ?.thumbnails
                        ?.medium
                        ?.url
                        .orEmpty(),
                publishedAt = DateUtil.getTimeAgo(it.snippet?.publishedAt.orEmpty()),
                isLive = it.snippet?.liveBroadcastContent?.lowercase() == "live",
            )
        } ?: listOf()

    return videos.sortedBy { it.isLive }
}
