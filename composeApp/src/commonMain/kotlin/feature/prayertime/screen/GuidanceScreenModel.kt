package feature.prayertime.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.data.DataState
import feature.prayertime.service.PrayerRepository
import feature.prayertime.service.model.Guidance
import feature.prayertime.service.model.GuidanceType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GuidanceScreenModel(
    private val repository: PrayerRepository,
) : StateScreenModel<GuidanceScreenModel.State>(State.Loading) {
    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val guidances: List<Guidance>,
        ) : State()
    }

    fun getGuidance(guidanceType: GuidanceType) {
        screenModelScope.launch {
            repository.fetchGuidance(guidanceType).collectLatest {
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
