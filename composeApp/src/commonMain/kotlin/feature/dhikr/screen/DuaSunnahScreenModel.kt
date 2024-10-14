package feature.dhikr.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.DataState
import feature.dhikr.service.DhikrRepository
import feature.dhikr.service.model.DuaCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DuaSunnahScreenModel(
    private val repository: DhikrRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = mutableState.asStateFlow()

    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val categories: List<DuaCategory>,
        ) : State()
    }

    fun getDoaCategories() {
        viewModelScope.launch {
            repository.fetchDuaCategories().collectLatest {
                mutableState.value =
                    when (it) {
                        is DataState.Error -> State.Error(it.message)
                        DataState.Loading -> State.Loading
                        is DataState.Success -> {
                            State.Content(it.data.filterNot { category -> category.tag == "quran" })
                        }
                    }
            }
        }
    }
}
