package feature.other.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import kotlinx.coroutines.launch

class SettingScreenModel(
    private val appRepository: AppRepository,
) : StateScreenModel<SettingScreenModel.State>(State()) {
    data class State(
        val appSetting: AppSetting? = null,
    )

    fun getSetting() {
        screenModelScope.launch {
            mutableState.value = state.value.copy(appSetting = appRepository.getSetting())
        }
    }

    fun updateTheme(theme: AppSetting.Theme) {
        screenModelScope.launch {
            appRepository.updateTheme(theme)

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
}
