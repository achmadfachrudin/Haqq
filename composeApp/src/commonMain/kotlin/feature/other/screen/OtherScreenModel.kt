package feature.other.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.data.DataState
import feature.other.service.AppRepository
import feature.other.service.entity.SettingEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OtherScreenModel(
    private val appRepository: AppRepository,
) : StateScreenModel<OtherScreenModel.State>(State.Loading) {
    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val setting: SettingEntity,
        ) : State()
    }

    enum class ClearType {
        DHIKR,
        DUA,
        QURAN,
        PRAYER_TIME,
        STUDY_NOTE,
        CLEAR_ALL,
    }

    fun clearData(clearType: ClearType) {
        screenModelScope.launch {
            when (clearType) {
                ClearType.DHIKR -> appRepository.clearDhikrData()
                ClearType.DUA -> appRepository.clearDuaData()
                ClearType.QURAN -> appRepository.clearQuranData()
                ClearType.PRAYER_TIME -> appRepository.clearPrayerTimeData()
                ClearType.STUDY_NOTE -> appRepository.clearNoteData()
                ClearType.CLEAR_ALL -> appRepository.clearAllData()
            }
        }
    }

    fun fetchSetting() {
        screenModelScope.launch {
            appRepository.fetchSetting().collectLatest {
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
