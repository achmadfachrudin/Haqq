package feature.dhikr.service.mapper

import core.util.orZero
import feature.dhikr.service.entity.DuaCategoryEntity
import feature.dhikr.service.entity.DuaCategoryRoom
import feature.dhikr.service.entity.DuaEntity
import feature.dhikr.service.entity.DuaRoom
import feature.dhikr.service.model.Dua
import feature.dhikr.service.model.DuaCategory
import feature.other.service.model.AppSetting

internal fun DuaEntity.mapToRoom(): DuaRoom =
    DuaRoom(
        id = id.orZero(),
        textIndopak = textIndopak.orEmpty(),
        textUthmani = textUthmani.orEmpty(),
        titleId = titleId.orEmpty(),
        titleEn = titleEn.orEmpty(),
        textTransliteration = textTransliteration.orEmpty(),
        textTranslationId = textTranslationId.orEmpty(),
        textTranslationEn = textTranslationEn.orEmpty(),
        hadithId = hadithId.orEmpty(),
        hadithEn = hadithEn.orEmpty(),
        tag = tag.orEmpty(),
    )

internal fun DuaRoom.mapToModel(setting: AppSetting): Dua {
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

    return Dua(
        id = id,
        title = title,
        textArabic = textArabic.replace("_n", "\n"),
        textTransliteration = textTransliteration.replace("_n", "\n"),
        textTranslation = textTranslation.replace("_n", "\n"),
        hadith = hadith.replace("_n", "\n"),
        tags = tag.split(","),
    )
}

internal fun DuaCategoryEntity.mapToRoom(): DuaCategoryRoom =
    DuaCategoryRoom(
        tag =
            tag
                .orEmpty()
                .lowercase()
                .trim()
                .replace(" ", "-"),
        titleId = titleId.orEmpty(),
        titleEn = titleEn.orEmpty(),
    )

internal fun DuaCategoryRoom.mapToModel(setting: AppSetting): DuaCategory {
    val title =
        when (setting.language) {
            AppSetting.Language.ENGLISH -> titleEn
            AppSetting.Language.INDONESIAN -> titleId
        }

    return DuaCategory(
        title = title,
        tag =
            tag
                .lowercase()
                .trim()
                .replace(" ", "-"),
    )
}
