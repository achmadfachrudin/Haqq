package feature.dhikr.service.mapper

import core.util.orZero
import feature.dhikr.service.entity.AsmaulHusnaEntity
import feature.dhikr.service.entity.AsmaulHusnaRoom
import feature.dhikr.service.model.AsmaulHusna
import feature.other.service.model.AppSetting

internal fun AsmaulHusnaEntity.mapToRoom(): AsmaulHusnaRoom =
    AsmaulHusnaRoom(
        id = id.orZero(),
        textIndopak = textIndopak.orEmpty(),
        textUthmani = textUthmani.orEmpty(),
        textTransliteration = textTransliteration.orEmpty(),
        textTranslationId = textTranslationId.orEmpty(),
        textTranslationEn = textTranslationEn.orEmpty(),
    )

internal fun AsmaulHusnaRoom.mapToModel(setting: AppSetting): AsmaulHusna {
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
