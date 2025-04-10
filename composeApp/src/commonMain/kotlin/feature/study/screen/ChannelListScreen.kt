package feature.study.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import core.ui.component.BaseItemCard
import core.ui.component.BaseOutlineTextField
import core.ui.component.BaseTopAppBar
import core.ui.component.ErrorState
import core.ui.component.LoadingState
import core.util.searchBy
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.search_channel
import haqq.composeapp.generated.resources.study_title
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object ChannelListNav

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChannelListScreen(
    onBackClick: () -> Unit,
    onVideoList: (VideoListNav) -> Unit,
) {
    val vm = koinViewModel<ChannelListScreenModel>()
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = stringResource(Res.string.study_title),
                onLeftButtonClick = {
                    onBackClick()
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val display = state) {
                is ChannelListScreenModel.State.Loading -> {
                    LoadingState()
                }

                is ChannelListScreenModel.State.Content -> {
                    val valueSearch = remember { mutableStateOf("") }
                    val query = valueSearch.value.lowercase()
                    val channelFiltered =
                        display.channels.filter {
                            it.name.searchBy(query)
                        }

                    LazyColumn {
                        stickyHeader {
                            Surface(Modifier.fillMaxWidth()) {
                                BaseOutlineTextField(
                                    modifier = Modifier.padding(16.dp),
                                    value = valueSearch.value,
                                    onValueChange = { newText ->
                                        valueSearch.value =
                                            newText
                                                .trim()
                                                .filter { it.isLetterOrDigit() }
                                    },
                                    label = stringResource(Res.string.search_channel),
                                    trailingClick = { valueSearch.value = "" },
                                    keyboardOptions =
                                        KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Done,
                                        ),
                                )
                            }
                        }
                        items(channelFiltered) { channel ->
                            BaseItemCard(
                                title = channel.name,
                                imageUrl = channel.image,
                                onClick = {
                                    onVideoList(
                                        VideoListNav(
                                            channelId = channel.id,
                                            channelName = channel.name,
                                            channelImage = channel.image,
                                            channelUrl = channel.url,
                                        ),
                                    )
                                },
                            )
                        }
                    }
                }

                is ChannelListScreenModel.State.Error -> {
                    ErrorState(display.message)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        vm.getChannels()
    }
}
