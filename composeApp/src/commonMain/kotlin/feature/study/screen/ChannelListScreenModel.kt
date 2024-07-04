package feature.study.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.data.DataState
import feature.study.service.StudyRepository
import feature.study.service.model.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChannelListScreenModel(
    private val repository: StudyRepository,
) : StateScreenModel<ChannelListScreenModel.State>(State.Loading) {
    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val channels: List<Channel>,
        ) : State()
    }

    fun getChannels() {
        screenModelScope.launch {
            repository.fetchChannels().collectLatest {
                mutableState.value =
                    when (it) {
                        is DataState.Error -> State.Error(it.message)
                        DataState.Loading -> State.Loading
                        is DataState.Success -> State.Content(it.data)
                    }
            }
        }
    }
}
