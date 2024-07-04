package feature.charity.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.data.DataState
import feature.charity.service.CharityRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharityListScreenModel(
    private val repository: CharityRepository,
) : StateScreenModel<CharityListScreenModel.State>(State.Loading) {
    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val images: List<String>,
        ) : State()
    }

    fun getCharity() {
        screenModelScope.launch {
            repository.fetchCharities().collectLatest {
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
