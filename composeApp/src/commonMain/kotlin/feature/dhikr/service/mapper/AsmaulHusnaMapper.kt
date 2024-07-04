package feature.dhikr.service.mapper

import core.util.orZero
import feature.dhikr.service.entity.AsmaulHusnaEntity
import feature.dhikr.service.entity.AsmaulHusnaRealm
import feature.dhikr.service.model.AsmaulHusna
import feature.other.service.model.AppSetting

internal fun AsmaulHusnaEntity.mapToRealm(): AsmaulHusnaRealm =
    AsmaulHusnaRealm().apply {
        id = this@mapToRealm.id.orZero()
        textIndopak = this@mapToRealm.textIndopak.orEmpty()
        textUthmani = this@mapToRealm.textUthmani.orEmpty()
        textTransliteration = this@mapToRealm.textTransliteration.orEmpty()
        textTranslationId = this@mapToRealm.textTranslationId.orEmpty()
        textTranslationEn = this@mapToRealm.textTranslationEn.orEmpty()
    }

internal fun AsmaulHusnaRealm.mapToModel(setting: AppSetting): AsmaulHusna {
    val textArabic =
        when {
            textIndopak.isNotEmpty() && textUthmani.isEmpty() -> textIndopak
            textIndopak.isEmpty() && textUthmani.isNotEmpty() -> textUthmani
            setting.arabicStyle == AppSetting.ArabicStyle.INDOPAK -> textIndopak
            setting.arabicStyle == AppSetting.ArabicStyle.UTHMANI -> textUthmani
            setting.arabicStyle == AppSetting.ArabicStyle.UTHMANI_TAJWEED -> textUthmani
            else -> textIndopak
        }

    val textTranslation =
        when (setting.language) {
            AppSetting.Language.ENGLISH -> textTranslationEn
            AppSetting.Language.INDONESIAN -> textTranslationId
        }

    return AsmaulHusna(
        id = id,
        textArabic = textArabic,
        textTransliteration = textTransliteration,
        textTranslation = textTranslation,
    )
}
