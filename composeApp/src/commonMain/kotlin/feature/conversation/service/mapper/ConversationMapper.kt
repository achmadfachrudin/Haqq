package feature.conversation.service.mapper

import core.util.orZero
import feature.conversation.service.entity.ConversationEntity
import feature.conversation.service.entity.ConversationRealm
import feature.conversation.service.model.Conversation
import feature.other.service.model.AppSetting

internal fun ConversationEntity.mapToRealm(): ConversationRealm =
    ConversationRealm().apply {
        id = this@mapToRealm.id.orZero()
        textIndopak = this@mapToRealm.textIndopak.orEmpty()
        textUthmani = this@mapToRealm.textUthmani.orEmpty()
        textTransliteration = this@mapToRealm.textTransliteration.orEmpty()
        textTranslationId = this@mapToRealm.textTranslationId.orEmpty()
        textTranslationEn = this@mapToRealm.textTranslationEn.orEmpty()
    }

internal fun ConversationRealm.mapToModel(setting: AppSetting): Conversation {
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

    return Conversation(
        id = id,
        textArabic = textArabic,
        textTransliteration = textTransliteration,
        textTranslation = textTranslation,
    )
}
