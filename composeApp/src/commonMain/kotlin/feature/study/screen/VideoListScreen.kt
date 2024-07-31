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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.ui.component.BaseImage
import core.ui.component.BaseText
import core.ui.component.BaseTopAppBar
import core.ui.component.LoadingState
import core.ui.theme.getHaqqTypography
import feature.study.service.model.Channel
import feature.study.service.model.Video
import feature.web.screen.WebScreen
import feature.web.screen.YoutubeScreen

class VideoListScreen(
    private val channel: Channel,
) : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<VideoListScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = channel.name,
                    onLeftButtonClick = {
                        navigator.pop()
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
                                        navigator.push(
                                            YoutubeScreen(
                                                video.id,
                                                video.title,
                                            ),
                                        )
                                    },
                                )
                            }
                        }
                    }

                    is VideoListScreenModel.State.Error -> {
                        navigator.replace(WebScreen(channel.url))
                    }
                }
            }
        }

        LaunchedEffect(currentCompositeKeyHash) {
            screenModel.onViewed(channel.id)
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
}
