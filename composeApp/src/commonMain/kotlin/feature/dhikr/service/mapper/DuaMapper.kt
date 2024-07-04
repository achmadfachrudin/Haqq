package feature.dhikr.service.mapper

import feature.dhikr.service.entity.DuaCategoryEntity
import feature.dhikr.service.entity.DuaCategoryRealm
import feature.dhikr.service.entity.DuaEntity
import feature.dhikr.service.entity.DuaRealm
import feature.dhikr.service.model.Dua
import feature.dhikr.service.model.DuaCategory
import feature.other.service.model.AppSetting

internal fun DuaEntity.mapToDuaRealm(): DuaRealm =
    DuaRealm().apply {
        id = this@mapToDuaRealm.id ?: 0
        textIndopak = this@mapToDuaRealm.textIndopak.orEmpty()
        textUthmani = this@mapToDuaRealm.textUthmani.orEmpty()
        titleId = this@mapToDuaRealm.titleId.orEmpty()
        titleEn = this@mapToDuaRealm.titleEn.orEmpty()
        textTransliteration = this@mapToDuaRealm.textTransliteration.orEmpty()
        textTranslationId = this@mapToDuaRealm.textTranslationId.orEmpty()
        textTranslationEn = this@mapToDuaRealm.textTranslationEn.orEmpty()
        hadithId = this@mapToDuaRealm.hadithId.orEmpty()
        hadithEn = this@mapToDuaRealm.hadithEn.orEmpty()
        tag = this@mapToDuaRealm.tag.orEmpty()
    }

internal fun DuaRealm.mapToModel(setting: AppSetting): Dua {
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

internal fun DuaCategoryEntity.mapToRealm(): DuaCategoryRealm =
    DuaCategoryRealm().apply {
        tag =
            this@mapToRealm
                .tag
                .orEmpty()
                .lowercase()
                .trim()
                .replace(" ", "-")
        titleId = this@mapToRealm.titleId.orEmpty()
        titleEn = this@mapToRealm.titleEn.orEmpty()
    }

internal fun DuaCategoryRealm.mapToModel(setting: AppSetting): DuaCategory {
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
