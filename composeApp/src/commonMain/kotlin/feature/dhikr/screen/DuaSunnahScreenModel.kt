package feature.dhikr.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.data.DataState
import feature.dhikr.service.DhikrRepository
import feature.dhikr.service.model.DuaCategory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DuaSunnahScreenModel(
    private val repository: DhikrRepository,
) : StateScreenModel<DuaSunnahScreenModel.State>(State.Loading) {
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
        screenModelScope.launch {
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
