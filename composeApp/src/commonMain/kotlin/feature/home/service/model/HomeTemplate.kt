package feature.home.service.model

import feature.dhikr.service.model.DhikrType
import org.jetbrains.compose.resources.StringResource

sealed class HomeTemplate {
    abstract val position: Int
    abstract val type: TemplateType
    abstract val label: String

    data class PrayerTime(
        override val position: Int,
        override val type: TemplateType = TemplateType.PRAYER_TIME,
        override val label: String,
        val date: String,
        val locationName: String,
        val nextPrayerName: StringResource,
        val nextPrayerTime: String,
    ) : HomeTemplate()

    data class Dhikr(
        override val position: Int,
        override val type: TemplateType = TemplateType.DHIKR,
        override val label: String,
        val dhikrType: DhikrType,
    ) : HomeTemplate()

    data class Menu(
        override val position: Int,
        override val type: TemplateType = TemplateType.MENU,
        override val label: String,
        val menus: String,
    ) : HomeTemplate()

    data class SingleImage(
        override val position: Int,
        override val type: TemplateType = TemplateType.SINGLE_IMAGE,
        override val label: String,
        val image: String,
        val link: String,
    ) : HomeTemplate()

    data class MultipleImage(
        override val position: Int,
        override val type: TemplateType = TemplateType.MULTIPLE_IMAGE,
        override val label: String,
        val images: List<String>,
        val links: List<String>,
    ) : HomeTemplate()

    data class Message(
        override val position: Int,
        override val type: TemplateType = TemplateType.MESSAGE,
        override val label: String,
        val textString: String,
        val textResource: StringResource? = null,
    ) : HomeTemplate()

    data class LastRead(
        override val position: Int,
        override val type: TemplateType = TemplateType.LAST_READ,
        override val label: String,
        val arabic: String,
        val chapterNumber: Int,
        val verseNumber: Int,
    ) : HomeTemplate()

    data class QuranVerse(
        override val position: Int,
        override val type: TemplateType = TemplateType.QURAN_VERSE,
        override val label: String,
        val translation: String,
        val chapterNumber: Int,
        val verseNumber: Int,
    ) : HomeTemplate()

    data class Video(
        override val position: Int,
        override val type: TemplateType = TemplateType.VIDEO,
        override val label: String,
        val image: String,
        val link: String,
    ) : HomeTemplate()
}
