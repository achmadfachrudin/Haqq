package feature.dhikr.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.DataState
import feature.dhikr.service.DhikrRepository
import feature.dhikr.service.model.Dhikr
import feature.dhikr.service.model.DhikrType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DhikrListScreenModel(
    private val repository: DhikrRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = mutableState.asStateFlow()

    private lateinit var dhikrType: DhikrType
    var currentPage = 0

    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val dhikrs: MutableList<Dhikr>,
        ) : State()
    }

    fun getDzikir(type: DhikrType) {
        viewModelScope.launch {
            dhikrType = type

            repository.fetchDhikr(dhikrType).collectLatest {
                mutableState.value =
                    when (it) {
                        is DataState.Error -> State.Error(it.message)
                        DataState.Loading -> State.Loading
                        is DataState.Success -> State.Content(it.data.toMutableList())
                    }
            }
        }
    }

    fun dhikrClicked(dhikr: Dhikr) {
        viewModelScope.launch {
            if (dhikr.count + 1 <= dhikr.maxCount) {
                val newDhikr = dhikr.copy(count = dhikr.count + 1)

                repository.updateDhikrCount(dhikrType, dhikr = dhikr)

                if (state.value is State.Content) {
                    val newList = (state.value as State.Content).dhikrs
                    val indexItem = newList.indexOfFirst { it.id == dhikr.id }
                    newList.removeAt(indexItem)
                    newList.add(indexItem, newDhikr)
                    mutableState.value = State.Content(newList)
                }
            }
        }
    }

    fun resetDhikrCount() {
        viewModelScope.launch {
            repository.resetDhikrCount(dhikrType)

            delay(200)
            getDzikir(dhikrType)
        }
    }
}
