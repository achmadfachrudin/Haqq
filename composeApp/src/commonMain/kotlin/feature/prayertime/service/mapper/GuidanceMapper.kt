package feature.prayertime.service.mapper

import core.util.orZero
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import feature.prayertime.service.entity.GuidanceEntity
import feature.prayertime.service.entity.GuidanceRealm
import feature.prayertime.service.model.Guidance
import feature.prayertime.service.model.GuidanceType
import org.koin.mp.KoinPlatform

internal fun GuidanceEntity.mapToGuidanceRealm(): GuidanceRealm {
    val itemType = this@mapToGuidanceRealm.type.orEmpty()
    val itemPosition = this@mapToGuidanceRealm.position.orZero()
    return GuidanceRealm().apply {
        id = "$itemType-$itemPosition"
        position = itemPosition
        type = itemType
        image = this@mapToGuidanceRealm.image.orEmpty()
        titleId = this@mapToGuidanceRealm.titleId.orEmpty()
        titleEn = this@mapToGuidanceRealm.titleEn.orEmpty()
        descId = this@mapToGuidanceRealm.descId.orEmpty()
        descEn = this@mapToGuidanceRealm.descEn.orEmpty()
        textIndopak = this@mapToGuidanceRealm.textIndopak.orEmpty()
        textUthmani = this@mapToGuidanceRealm.textUthmani.orEmpty()
        textTransliteration = this@mapToGuidanceRealm.textTransliteration.orEmpty()
        textTranslationId = this@mapToGuidanceRealm.textTranslationId.orEmpty()
        textTranslationEn = this@mapToGuidanceRealm.textTranslationEn.orEmpty()
        hadithId = this@mapToGuidanceRealm.hadithId.orEmpty()
        hadithEn = this@mapToGuidanceRealm.hadithEn.orEmpty()
    }
}

internal fun GuidanceRealm.mapToModel(): Guidance {
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()
    val setting = appRepository.getSetting()

    val title =
        when (setting.language) {
            AppSetting.Language.ENGLISH -> titleEn
            AppSetting.Language.INDONESIAN -> titleId
        }
    val desc =
        when (setting.language) {
            AppSetting.Language.ENGLISH -> descEn
            AppSetting.Language.INDONESIAN -> descId
        }
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
    val hadith =
        when (setting.language) {
            AppSetting.Language.ENGLISH -> hadithEn
            AppSetting.Language.INDONESIAN -> hadithId
        }

    return Guidance(
        position = position,
        type = enumValueOf<GuidanceType>(type),
        image = image,
        title = title,
        desc = desc,
        textArabic = textArabic.replace("_n", "\n"),
        textTransliteration = textTransliteration.replace("_n", "\n"),
        textTranslation = textTranslation.replace("_n", "\n"),
        hadith = hadith.replace("_n", "\n"),
    )
}
