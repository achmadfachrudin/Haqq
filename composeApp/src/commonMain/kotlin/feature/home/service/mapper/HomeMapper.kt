package feature.home.service.mapper

import core.util.orZero
import feature.dhikr.service.model.DhikrType
import feature.home.service.entity.HomeTemplateEntity
import feature.home.service.entity.HomeTemplateRoom
import feature.home.service.model.HomeTemplate
import feature.home.service.model.TemplateType
import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import feature.prayertime.service.model.PrayerTime
import feature.prayertime.service.model.Salah
import feature.quran.service.model.Verse
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.prayer_enable_gps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.mp.KoinPlatform

internal fun HomeTemplateEntity.mapToRoom(): HomeTemplateRoom {
    val itemType = type.orEmpty()
    val itemPosition = position.orZero()
    return HomeTemplateRoom(
        position = itemPosition,
        type = itemType,
        labelId = label?.id.orEmpty(),
        labelEn = label?.en.orEmpty(),
        textId = text?.id.orEmpty(),
        textEn = text?.en.orEmpty(),
        images = images.orEmpty(),
        links = links.orEmpty(),
    )
}

internal suspend fun List<HomeTemplateRoom>.mapToModel(
    lastReadVerse: Verse,
    quranVerse: Verse,
    prayerTimeToday: PrayerTime?,
    prayerTimeNext: Triple<Salah, String, Boolean>,
    dhikrType: DhikrType?,
): List<HomeTemplate> =
    withContext(Dispatchers.IO) {
        val appRepository = KoinPlatform.getKoin().get<AppRepository>()
        val language = appRepository.getSetting().language

        val templates = mutableListOf<HomeTemplate>()

        this@mapToModel
            .sortedBy { it.position }
            .forEach {
                val position = it.position.orZero()
                val type = enumValueOf<TemplateType>(it.type)
                val label =
                    when (language) {
                        AppSetting.Language.ENGLISH -> it.labelEn
                        AppSetting.Language.INDONESIAN -> it.labelId
                    }
                val text =
                    when (language) {
                        AppSetting.Language.ENGLISH -> it.textEn
                        AppSetting.Language.INDONESIAN -> it.textId
                    }
                val images =
                    it.images.takeIf { images -> images.isNotEmpty() }?.split(",") ?: listOf()
                val links =
                    it.links.takeIf { links -> links.isNotEmpty() }?.split(",") ?: listOf()

                when (type) {
                    TemplateType.PRAYER_TIME -> {
                        prayerTimeToday?.let { today ->
                            val prayerTimeDate =
                                when (appRepository.getSetting().language) {
                                    AppSetting.Language.ENGLISH -> today.day.dayNameEn
                                    AppSetting.Language.INDONESIAN -> today.day.dayNameId
                                }

                            templates.add(
                                HomeTemplate.PrayerTime(
                                    position = position,
                                    type = type,
                                    label = label,
                                    date = prayerTimeDate,
                                    locationName = today.locationName,
                                    nextPrayerName = prayerTimeNext.first.titleRes,
                                    nextPrayerTime = prayerTimeNext.second,
                                ),
                            )
                        } ?: run {
                            templates.add(
                                HomeTemplate.Message(
                                    position = position,
                                    type = type,
                                    label = label,
                                    textString = text,
                                    textResource = Res.string.prayer_enable_gps,
                                ),
                            )
                        }
                    }

                    TemplateType.DHIKR -> {
                        dhikrType?.let { dhikrType ->
                            templates.add(
                                HomeTemplate.Dhikr(
                                    position = position,
                                    type = type,
                                    label = label,
                                    dhikrType = dhikrType,
                                ),
                            )
                        }
                    }

                    TemplateType.MENU -> {
                        if (false) {
                            templates.add(
                                HomeTemplate.Menu(
                                    position = position,
                                    type = type,
                                    label = label,
                                    menus = "",
                                ),
                            )
                        }
                    }

                    TemplateType.SINGLE_IMAGE -> {
                        if (images.isNotEmpty() && links.isNotEmpty()) {
                            templates.add(
                                HomeTemplate.SingleImage(
                                    position = position,
                                    type = type,
                                    label = label,
                                    image = images.first(),
                                    link = links.first(),
                                ),
                            )
                        }
                    }

                    TemplateType.MULTIPLE_IMAGE -> {
                        if (images.isNotEmpty() && links.isNotEmpty()) {
                            templates.add(
                                HomeTemplate.MultipleImage(
                                    position = position,
                                    type = type,
                                    label = label,
                                    images = images,
                                    links = links,
                                ),
                            )
                        }
                    }

                    TemplateType.MESSAGE -> {
                        if (text.isNotEmpty()) {
                            templates.add(
                                HomeTemplate.Message(
                                    position = position,
                                    type = type,
                                    label = label,
                                    textString = text,
                                ),
                            )
                        }
                    }

                    TemplateType.LAST_READ -> {
                        val chapterId = lastReadVerse.chapterId
                        val verseNumber = lastReadVerse.verseNumber
                        val textArabic = lastReadVerse.textArabic

                        templates.add(
                            HomeTemplate.LastRead(
                                position = position,
                                type = type,
                                label = label,
                                arabic = textArabic,
                                chapterNumber = chapterId,
                                verseNumber = verseNumber,
                            ),
                        )
                    }

                    TemplateType.QURAN_VERSE -> {
                        templates.add(
                            HomeTemplate.QuranVerse(
                                position = position,
                                type = type,
                                label = label,
                                translation = quranVerse.textTranslation,
                                chapterNumber = quranVerse.chapterId,
                                verseNumber = quranVerse.verseNumber,
                            ),
                        )
                    }

                    TemplateType.VIDEO -> {
                        if (images.isNotEmpty() && links.isNotEmpty()) {
                            templates.add(
                                HomeTemplate.Video(
                                    position = position,
                                    type = type,
                                    label = label,
                                    image = images.first(),
                                    link = links.first(),
                                ),
                            )
                        }
                    }

                    TemplateType.UNKNOWN -> {
                    }
                }
            }

        templates
    }
