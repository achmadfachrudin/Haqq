package feature.study.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import core.ui.component.BaseImage
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.component.LoadingState
import core.ui.theme.getHaqqTypography
import feature.study.service.model.Video
import feature.web.screen.WebNav
import feature.web.screen.YoutubeNav
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data class VideoListNav(
    val channelId: String,
    val channelName: String,
    val channelImage: String,
    val channelUrl: String,
)

@Composable
fun VideoListScreen(
    nav: VideoListNav,
    onBackClick: () -> Unit,
    onWebClick: (WebNav) -> Unit,
    onYoutubeClick: (YoutubeNav) -> Unit,
) {
    val vm = koinViewModel<VideoListScreenModel>()
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = nav.channelName,
                onLeftButtonClick = {
                    onBackClick()
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val display = state) {
                is VideoListScreenModel.State.Loading -> {
                    LoadingState()
                }

                is VideoListScreenModel.State.Content -> {
                    LazyColumn {
                        items(display.videos) { video ->
                            VideoCard(
                                video = video,
                                onClick = {
                                    onYoutubeClick(
                                        YoutubeNav(
                                            videoId = video.id,
                                            title = video.title,
                                        ),
                                    )
                                },
                            )
                        }
                    }
                }

                is VideoListScreenModel.State.Error -> {
                    onWebClick(WebNav(url = nav.channelUrl))
                }
            }
        }
    }

    LaunchedEffect(currentCompositeKeyHash) {
        vm.onViewed(nav.channelId)
    }
}

@Composable
private fun VideoCard(
    video: Video,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickable { onClick() }.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BaseImage(
            modifier = Modifier.weight(0.4f).clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.FillWidth,
            imageUrl = video.thumbnailUrl,
            contentDescription = video.title,
        )

        Column(modifier = Modifier.weight(0.6f).padding(start = 16.dp)) {
            BaseText(
                video.title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = getHaqqTypography().titleSmall,
            )
            BaseText(text = video.publishedAt, style = getHaqqTypography().bodySmall)
        }
    }
}
