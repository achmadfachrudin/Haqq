package feature.other.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import feature.quran.service.QuranRepository
import feature.quran.service.model.Verse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingScreenModel(
    private val appRepository: AppRepository,
    private val quranRepository: QuranRepository,
) : StateScreenModel<SettingScreenModel.State>(State()) {
    data class State(
        val appSetting: AppSetting? = null,
        val verse: Verse? = null,
    )

    fun getSetting() {
        screenModelScope.launch {
            mutableState.value =
                state.value.copy(appSetting = appRepository.getSetting(), verse = null)

            delay(500)

            getVerse()
        }
    }

    fun updateTheme(theme: AppSetting.Theme) {
        screenModelScope.launch {
            appRepository.updateTheme(theme)

            getSetting()
        }
    }

    fun updateThemeColor(themeColor: AppSetting.ThemeColor) {
        screenModelScope.launch {
            appRepository.updateThemeColor(themeColor)

            getSetting()
        }
    }

    fun updateLanguage(language: AppSetting.Language) {
        screenModelScope.launch {
            appRepository.updateLanguage(language)

            getSetting()
        }
    }

    fun updateArabicStyle(style: AppSetting.ArabicStyle) {
        screenModelScope.launch {
            appRepository.updateArabicStyle(style)

            getSetting()
        }
    }

    fun updateTransliterationVisibility(shouldShow: Boolean) {
        screenModelScope.launch {
            appRepository.updateTransliterationVisibility(shouldShow)

            getSetting()
        }
    }

    fun updateTranslationVisibility(shouldShow: Boolean) {
        screenModelScope.launch {
            appRepository.updateTranslationVisibility(shouldShow)

            getSetting()
        }
    }

    fun updateArabicFontSize(fontSize: Int) {
        screenModelScope.launch {
            appRepository.updateArabicFontSize(fontSize)

            getSetting()
        }
    }

    fun updateTransliterationFontSize(fontSize: Int) {
        screenModelScope.launch {
            appRepository.updateTransliterationFontSize(fontSize)

            getSetting()
        }
    }

    fun updateTranslationFontSize(fontSize: Int) {
        screenModelScope.launch {
            appRepository.updateTranslationFontSize(fontSize)

            getSetting()
        }
    }

    private fun getVerse() {
        screenModelScope.launch {
            val verse = quranRepository.getVerseById(5)
            mutableState.value = state.value.copy(verse = verse)
        }
    }
}
