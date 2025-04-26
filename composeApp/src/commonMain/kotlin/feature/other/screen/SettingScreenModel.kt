package feature.other.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import feature.quran.service.QuranRepository
import feature.quran.service.model.Verse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingScreenModel(
    private val appRepository: AppRepository,
    private val quranRepository: QuranRepository,
) : ViewModel() {
    val mutableState = MutableStateFlow(State())
    val state: StateFlow<State> = mutableState.asStateFlow()

    data class State(
        val appSetting: AppSetting? = null,
        val verse: Verse? = null,
    )

    fun getSetting() {
        viewModelScope.launch {
            mutableState.value =
                state.value.copy(appSetting = appRepository.getSetting(), verse = null)

            delay(300)

            getVerse()
        }
    }

    fun updateTheme(theme: AppSetting.Theme) {
        viewModelScope.launch {
            appRepository.updateTheme(theme)

            getSetting()
        }
    }

    fun updateThemeColor(themeColor: AppSetting.ThemeColor) {
        viewModelScope.launch {
            appRepository.updateThemeColor(themeColor)

            getSetting()
        }
    }

    fun updateLanguage(language: AppSetting.Language) {
        viewModelScope.launch {
            appRepository.updateLanguage(language)

            getSetting()
        }
    }

    fun updateArabicStyle(style: AppSetting.ArabicStyle) {
        viewModelScope.launch {
            appRepository.updateArabicStyle(style)

            getSetting()
        }
    }

    fun updateTransliterationVisibility(shouldShow: Boolean) {
        viewModelScope.launch {
            appRepository.updateTransliterationVisibility(shouldShow)

            getSetting()
        }
    }

    fun updateTranslationVisibility(shouldShow: Boolean) {
        viewModelScope.launch {
            appRepository.updateTranslationVisibility(shouldShow)

            getSetting()
        }
    }

    fun updateArabicFontSize(fontSize: Int) {
        viewModelScope.launch {
            appRepository.updateArabicFontSize(fontSize)

            getSetting()
        }
    }

    fun updateTransliterationFontSize(fontSize: Int) {
        viewModelScope.launch {
            appRepository.updateTransliterationFontSize(fontSize)

            getSetting()
        }
    }

    fun updateTranslationFontSize(fontSize: Int) {
        viewModelScope.launch {
            appRepository.updateTranslationFontSize(fontSize)

            getSetting()
        }
    }

    private fun getVerse() {
        viewModelScope.launch {
            val verse = quranRepository.getVerseById(5)
            mutableState.value = state.value.copy(verse = verse)
        }
    }
}
