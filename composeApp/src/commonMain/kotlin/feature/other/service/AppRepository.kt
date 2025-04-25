package feature.other.service

import core.data.ApiResponse
import core.data.DataState
import data.AppDatabase
import feature.other.service.entity.AppSettingRealm
import feature.other.service.model.AppSetting
import feature.other.service.source.remote.SettingRemote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class AppRepository(
    private val remote: SettingRemote,
    private val database: AppDatabase,
) {
    fun getSetting(): AppSetting {
        return runBlocking {
            withContext(Dispatchers.IO) {
                val appSettingRoom =
                    database.appSettingDao().getAll().firstOrNull()

                if (appSettingRoom == null) {
                    database.appSettingDao().insert(AppSettingRealm())
                }

                val latestSetting = database.appSettingDao().getAll().first()

                return@withContext AppSetting(
                    language = AppSetting.Language.valueOf(latestSetting.languageName),
                    theme = AppSetting.Theme.valueOf(latestSetting.theme),
                    themeColor = AppSetting.ThemeColor.valueOf(latestSetting.themeColor),
                    arabicStyle = AppSetting.ArabicStyle.valueOf(latestSetting.arabicStyleName),
                    arabicFontSize = latestSetting.arabicFontSize,
                    transliterationVisibility = latestSetting.transliterationVisibility,
                    transliterationFontSize = latestSetting.transliterationFontSize,
                    translationVisibility = latestSetting.translationVisibility,
                    translationFontSize = latestSetting.translationFontSize,
                    location =
                        AppSetting.Location(
                            latitude = latestSetting.locationLatitude,
                            longitude = latestSetting.locationLongitude,
                            name = latestSetting.locationName,
                        ),
                    acceptPrivacyPolicy = latestSetting.acceptPrivacyPolicy,
                    lastUpdate = LocalDate.parse(latestSetting.lastUpdate),
                )
            }
        }
    }

    fun updateLanguage(language: AppSetting.Language) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    languageName = language.name,
                )

            database.appSettingDao().update(updated)
        }
    }

    fun updateTheme(theme: AppSetting.Theme) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    theme = theme.name,
                )

            database.appSettingDao().update(updated)
        }
    }

    fun updateThemeColor(themeColor: AppSetting.ThemeColor) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    themeColor = themeColor.name,
                )

            database.appSettingDao().update(updated)
        }
    }

    fun updateArabicStyle(arabicStyle: AppSetting.ArabicStyle) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    arabicStyleName = arabicStyle.name,
                )

            database.appSettingDao().update(updated)
        }
    }

    fun updateArabicFontSize(fontSize: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    arabicFontSize = fontSize,
                )

            database.appSettingDao().update(updated)
        }
    }

    fun updateTranslationFontSize(fontSize: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    translationFontSize = fontSize,
                )

            database.appSettingDao().update(updated)
        }
    }

    fun updateTranslationVisibility(show: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    translationVisibility = show,
                )

            database.appSettingDao().update(updated)
        }
    }

    fun updateTransliterationFontSize(fontSize: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    transliterationFontSize = fontSize,
                )

            database.appSettingDao().update(updated)
        }
    }

    fun updateTransliterationVisibility(show: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    transliterationVisibility = show,
                )

            database.appSettingDao().update(updated)
        }
    }

    fun updateLocation(location: AppSetting.Location) {
        CoroutineScope(Dispatchers.IO).launch {
            if (location.latitude != 0.0 &&
                location.longitude != 0.0 &&
                location.name.isNotEmpty()
            ) {
                val current = database.appSettingDao().getAll().first()
                val updated =
                    current.copy(
                        locationLatitude = location.latitude,
                        locationLongitude = location.longitude,
                        locationName = location.name,
                    )

                database.appSettingDao().update(updated)
            }
        }
    }

    fun updateAcceptPrivacyPolicy() {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    acceptPrivacyPolicy = true,
                )

            database.appSettingDao().update(updated)
        }
    }

    fun updateLastUpdate(lastUpdate: LocalDate) {
        CoroutineScope(Dispatchers.IO).launch {
            val current = database.appSettingDao().getAll().first()
            val updated =
                current.copy(
                    lastUpdate = lastUpdate.toString(),
                )
            database.appSettingDao().update(updated)
        }
    }

    fun clearDhikrData() {
        CoroutineScope(Dispatchers.IO).launch {
            database.dhikrDao().deleteAll()
        }
    }

    fun clearDuaData() {
        CoroutineScope(Dispatchers.IO).launch {
            database.duaCategoryDao().deleteAll()
            database.duaDao().deleteAll()
        }
    }

    fun clearAsmaulHusnaData() {
        CoroutineScope(Dispatchers.IO).launch {
            database.asmaulHusnaDao().deleteAll()
        }
    }

    fun clearQuranData() {
        CoroutineScope(Dispatchers.IO).launch {
            database.chapterDao().deleteAll()
            database.verseDao().deleteAll()
            database.verseFavoriteDao().deleteAll()
            database.juzDao().deleteAll()
            database.lastReadDao().deleteAll()
        }
    }

    fun clearPrayerTimeData() {
        CoroutineScope(Dispatchers.IO).launch {
            database.prayerTimeDao().deleteAll()
        }
    }

    fun clearConversationData() {
        CoroutineScope(Dispatchers.IO).launch {
            database.conversationDao().deleteAll()
        }
    }

    fun clearNoteData() {
        CoroutineScope(Dispatchers.IO).launch {
            database.noteDao().deleteAll()
        }
    }

    fun clearAllData() {
        CoroutineScope(Dispatchers.IO).launch {
            database.appSettingDao().deleteAll()
            database.asmaulHusnaDao().deleteAll()
            database.chapterDao().deleteAll()
            database.conversationDao().deleteAll()
            database.dhikrDao().deleteAll()
            database.duaDao().deleteAll()
            database.duaCategoryDao().deleteAll()
            database.guidanceDao().deleteAll()
            database.homeTemplateDao().deleteAll()
            database.juzDao().deleteAll()
            database.lastReadDao().deleteAll()
            database.noteDao().deleteAll()
            database.pageDao().deleteAll()
            database.prayerTimeDao().deleteAll()
            database.verseDao().deleteAll()
            database.verseFavoriteDao().deleteAll()
        }
    }

    fun fetchSetting() =
        flow {
            when (val result = remote.fetchSetting()) {
                is ApiResponse.Error -> emit(DataState.Error(result.message))
                is ApiResponse.Success -> {
                    val remoteResult = result.body.first()

                    emit(DataState.Success(remoteResult))
                }
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)
}
