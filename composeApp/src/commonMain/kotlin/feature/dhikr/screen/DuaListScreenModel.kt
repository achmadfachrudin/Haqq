package feature.dhikr.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.DataState
import feature.dhikr.service.DhikrRepository
import feature.dhikr.service.model.Dua
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DuaListScreenModel(
    private val repository: DhikrRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = mutableState.asStateFlow()
    var shouldRefresh by mutableStateOf(true)

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
        viewModelScope.launch {
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
}
