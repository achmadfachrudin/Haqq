@file:OptIn(ExperimentalTime::class)

package feature.dhikr.service.mapper

import core.util.orZero
import feature.dhikr.service.entity.DhikrEntity
import feature.dhikr.service.entity.DhikrRoom
import feature.dhikr.service.model.Dhikr
import feature.dhikr.service.model.DhikrType
import feature.other.service.model.AppSetting
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal fun DhikrEntity.mapToRoom(dhikrType: DhikrType): DhikrRoom {
    val dhikrId = id ?: 0
    val dhikrTypeName = dhikrType.name
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    return DhikrRoom(
        pkey = "$dhikrTypeName-$dhikrId",
        id = dhikrId,
        type = dhikrTypeName,
        textIndopak = textIndopak.orEmpty(),
        textUthmani = textUthmani.orEmpty(),
        titleId = titleId.orEmpty(),
        titleEn = titleEn.orEmpty(),
        textTranslationId = textTranslationId.orEmpty(),
        textTranslationEn = textTranslationEn.orEmpty(),
        hadithId = hadithId.orEmpty(),
        hadithEn = hadithEn.orEmpty(),
        count = 0,
        maxCount = maxCount.orZero(),
        latestUpdate = today.toString(),
    )
}

internal fun DhikrRoom.mapToModel(setting: AppSetting): Dhikr {
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
