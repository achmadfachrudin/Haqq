package feature.dhikr.service.mapper

import feature.dhikr.service.entity.DhikrEntity
import feature.dhikr.service.entity.DhikrRealm
import feature.dhikr.service.model.Dhikr
import feature.dhikr.service.model.DhikrType
import feature.other.service.model.AppSetting
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

internal fun DhikrEntity.mapToRealm(dhikrType: DhikrType): DhikrRealm {
    val dhikrId = this@mapToRealm.id ?: 0
    val dhikrTypeName = dhikrType.name
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    return DhikrRealm().apply {
        key = "$dhikrTypeName-$dhikrId"
        id = dhikrId
        type = dhikrTypeName
        textIndopak = this@mapToRealm.textIndopak.orEmpty()
        textUthmani = this@mapToRealm.textUthmani.orEmpty()
        titleId = this@mapToRealm.titleId.orEmpty()
        titleEn = this@mapToRealm.titleEn.orEmpty()
        textTranslationId = this@mapToRealm.textTranslationId.orEmpty()
        textTranslationEn = this@mapToRealm.textTranslationEn.orEmpty()
        hadithId = this@mapToRealm.hadithId.orEmpty()
        hadithEn = this@mapToRealm.hadithEn.orEmpty()
        count = 0
        maxCount = this@mapToRealm.maxCount ?: 0
        latestUpdate = today.toString()
    }
}

internal fun DhikrRealm.mapToModel(setting: AppSetting): Dhikr {
    val title =
        when (setting.language) {
            AppSetting.Language.ENGLISH -> titleEn
            AppSetting.Language.INDONESIAN -> titleId
        }
    val textArabic =
        when (setting.arabicStyle) {
            AppSetting.ArabicStyle.INDOPAK -> textIndopak
            AppSetting.ArabicStyle.UTHMANI -> textUthmani
            AppSetting.ArabicStyle.UTHMANI_TAJWEED -> textUthmani
        }
    val textTranslation =
        when (setting.language) {
            AppSetting.Language.ENGLISH -> textTranslationEn
            AppSetting.Language.INDONESIAN -> textTranslationId
        }
    val hadith =
        when (setting.language) {
            AppSetting.Language.ENGLISH -> hadithEn
            AppSetting.Language.INDONESIAN -> hadithId
        }

    val latestUpdate = LocalDate.parse(latestUpdate)
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    val newLatestUpdate: LocalDate
    val newCount: Int

    if (latestUpdate == today) {
        newLatestUpdate = today
        newCount = count
    } else {
        newLatestUpdate = latestUpdate
        newCount = 0
    }

    return Dhikr(
        id = id,
        title = title,
        textArabic = textArabic.replace("_n", "\n"),
        textTransliteration = textTransliteration.replace("_n", "\n"),
        textTranslation = textTranslation.replace("_n", "\n"),
        hadith = hadith.replace("_n", "\n"),
        maxCount = maxCount,
        count = newCount,
        latestUpdate = newLatestUpdate,
    )
}
