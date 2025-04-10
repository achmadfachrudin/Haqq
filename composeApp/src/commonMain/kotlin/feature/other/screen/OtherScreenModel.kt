package feature.other.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.data.DataState
import feature.other.service.AppRepository
import feature.other.service.entity.SettingEntity
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.clear_all_data
import haqq.composeapp.generated.resources.dhikr
import haqq.composeapp.generated.resources.dua
import haqq.composeapp.generated.resources.prayer_time_title
import haqq.composeapp.generated.resources.quran_title
import haqq.composeapp.generated.resources.study_note_title
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class OtherScreenModel(
    private val appRepository: AppRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = mutableState.asStateFlow()

    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val setting: SettingEntity,
        ) : State()
    }

    enum class ClearType(
        val label: StringResource,
    ) {
        DHIKR(Res.string.dhikr),
        DUA(Res.string.dua),
        QURAN(Res.string.quran_title),
        PRAYER_TIME(Res.string.prayer_time_title),
        STUDY_NOTE(Res.string.study_note_title),
        CLEAR_ALL(Res.string.clear_all_data),
    }

    fun clearData(clearType: ClearType) {
        viewModelScope.launch {
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
        viewModelScope.launch {
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
