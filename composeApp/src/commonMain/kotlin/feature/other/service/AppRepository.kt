package feature.other.service

import core.data.ApiResponse
import core.data.DataState
import feature.conversation.service.entity.ConversationRealm
import feature.dhikr.service.entity.AsmaulHusnaRealm
import feature.dhikr.service.entity.DhikrRealm
import feature.dhikr.service.entity.DuaCategoryRealm
import feature.dhikr.service.entity.DuaRealm
import feature.other.service.entity.AppSettingRealm
import feature.other.service.model.AppSetting
import feature.other.service.source.remote.SettingRemote
import feature.prayertime.service.entity.PrayerTimeRealm
import feature.quran.service.entity.ChapterRealm
import feature.quran.service.entity.JuzRealm
import feature.quran.service.entity.LastReadRealm
import feature.quran.service.entity.VerseFavoriteRealm
import feature.quran.service.entity.VerseRealm
import feature.study.service.entity.NoteRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.LocalDate

class AppRepository(
    private val remote: SettingRemote,
    private val realm: Realm,
) {
    fun getSetting(): AppSetting {
        if (realm.query<AppSettingRealm>().find().isEmpty()) {
            realm.writeBlocking {
                copyToRealm(AppSettingRealm())
            }
        }

        val setting = realm.query<AppSettingRealm>().find().first()

        return AppSetting(
            language = AppSetting.Language.valueOf(setting.languageName),
            theme = AppSetting.Theme.valueOf(setting.theme),
            themeColor = AppSetting.ThemeColor.valueOf(setting.themeColor),
            arabicStyle = AppSetting.ArabicStyle.valueOf(setting.arabicStyleName),
            arabicFontSize = setting.arabicFontSize,
            transliterationVisibility = setting.transliterationVisibility,
            transliterationFontSize = setting.transliterationFontSize,
            translationVisibility = setting.translationVisibility,
            translationFontSize = setting.translationFontSize,
            location =
                AppSetting.Location(
                    latitude = setting.locationLatitude,
                    longitude = setting.locationLongitude,
                    name = setting.locationName,
                ),
            acceptPrivacyPolicy = setting.acceptPrivacyPolicy,
            lastUpdate = LocalDate.parse(setting.lastUpdate),
        )
    }

    fun updateLanguage(language: AppSetting.Language) {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()

            setting.languageName = language.name
        }
    }

    fun updateTheme(theme: AppSetting.Theme) {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()

            setting.theme = theme.name
        }
    }

    fun updateThemeColor(themeColor: AppSetting.ThemeColor) {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()

            setting.themeColor = themeColor.name
        }
    }

    fun updateArabicStyle(arabicStyle: AppSetting.ArabicStyle) {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()

            setting.arabicStyleName = arabicStyle.name
        }
    }

    fun updateArabicFontSize(fontSize: Int) {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()

            setting.arabicFontSize = fontSize
        }
    }

    fun updateTranslationFontSize(fontSize: Int) {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()

            setting.translationFontSize = fontSize
        }
    }

    fun updateTranslationVisibility(show: Boolean) {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()

            setting.translationVisibility = show
        }
    }

    fun updateTransliterationFontSize(fontSize: Int) {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()

            setting.transliterationFontSize = fontSize
        }
    }

    fun updateTransliterationVisibility(show: Boolean) {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()

            setting.transliterationVisibility = show
        }
    }

    fun updateLocation(location: AppSetting.Location) {
        if (location.latitude != 0.0 &&
            location.longitude != 0.0 &&
            location.name.isNotEmpty()
        ) {
            realm.writeBlocking {
                val setting = this.query<AppSettingRealm>().find().first()

                setting.locationLatitude = location.latitude
                setting.locationLongitude = location.longitude
                setting.locationName = location.name
            }
        }
    }

    fun updateAcceptPrivacyPolicy() {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()

            setting.acceptPrivacyPolicy = true
        }
    }

    fun updateLastUpdate(lastUpdate: LocalDate) {
        realm.writeBlocking {
            val setting = this.query<AppSettingRealm>().find().first()
            setting.lastUpdate = lastUpdate.toString()
        }
    }

    fun clearDhikrData() {
        realm.writeBlocking {
            delete(DhikrRealm::class)
        }
    }

    fun clearDuaData() {
        realm.writeBlocking {
            delete(DuaCategoryRealm::class)
            delete(DuaRealm::class)
        }
    }

    fun clearAsmaulHusnaData() {
        realm.writeBlocking {
            delete(AsmaulHusnaRealm::class)
        }
    }

    fun clearQuranData() {
        realm.writeBlocking {
            delete(ChapterRealm::class)
            delete(VerseRealm::class)
            delete(VerseFavoriteRealm::class)
            delete(LastReadRealm::class)
            delete(JuzRealm::class)
        }
    }

    fun clearPrayerTimeData() {
        realm.writeBlocking {
            delete(PrayerTimeRealm::class)
        }
    }

    fun clearConversationData() {
        realm.writeBlocking {
            delete(ConversationRealm::class)
        }
    }

    fun clearNoteData() {
        realm.writeBlocking {
            delete(NoteRealm::class)
        }
    }

    fun clearAllData() {
        realm.writeBlocking {
            deleteAll()
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
