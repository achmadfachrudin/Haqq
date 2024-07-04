package feature.study.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.data.DataState
import feature.study.service.StudyRepository
import feature.study.service.model.Video
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class VideoListScreenModel(
    private val repository: StudyRepository,
) : StateScreenModel<VideoListScreenModel.State>(State.Loading) {
    var cId: String = ""
    var apiList: List<String> = listOf()
    var apiSelected: String = ""
    val apiSelectedIndex: Int
        get() = apiList.indexOf(apiSelected)

    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val videos: List<Video>,
        ) : State()
    }

    fun onViewed(channelId: String) {
        screenModelScope.launch {
            cId = channelId
            apiList = repository.fetchYoutubeAPI().last()
            apiSelected = apiList.firstOrNull().orEmpty()
            getVideos()
        }
    }

    private fun getVideos() {
        screenModelScope.launch {
            repository.fetchVideoList(apiKey = apiSelected, channelId = cId).collectLatest {
                when (it) {
                    is DataState.Error -> {
                        if (apiSelectedIndex != apiList.lastIndex) {
                            apiSelected = apiList[apiSelectedIndex + 1]
                            getVideos()
                        } else {
                            mutableState.value = State.Error(it.message)
                        }
                    }

                    DataState.Loading -> {
                    }
                    is DataState.Success -> mutableState.value = State.Content(it.data)
                }
            }
        }
    }
}
