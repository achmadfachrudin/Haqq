package feature.dhikr.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.data.DataState
import feature.dhikr.service.DhikrRepository
import feature.dhikr.service.model.Dua
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DuaListScreenModel(
    private val repository: DhikrRepository,
) : StateScreenModel<DuaListScreenModel.State>(State.Loading) {
    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val duas: List<Dua>,
        ) : State()
    }

    fun getDuaByTag(tag: String) {
        screenModelScope.launch {
            repository.fetchDuaByTag(tag).collectLatest {
                mutableState.value =
                    when (it) {
                        is DataState.Error -> State.Error(it.message)
                        DataState.Loading -> State.Loading
                        is DataState.Success -> State.Content(it.data)
                    }
            }
        }
    }

    fun getDuaList(): List<Dua> =
        if (state.value is State.Content) {
            (state.value as State.Content).duas
        } else {
            listOf()
        }
}
