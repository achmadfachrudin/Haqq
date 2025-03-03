package feature.home.service.mapper

import core.util.orZero
import feature.dhikr.service.model.DhikrType
import feature.home.service.entity.HomeTemplateEntity
import feature.home.service.entity.HomeTemplateRealm
import feature.home.service.model.HomeTemplate
import feature.home.service.model.TemplateType
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppSetting
import feature.other.service.model.AppString
import feature.prayertime.service.PrayerRepository
import feature.prayertime.service.model.Salah
import feature.quran.service.QuranRepository
import feature.quran.service.model.Verse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.koin.mp.KoinPlatform

internal fun HomeTemplateEntity.mapToHomeTemplateRealm(): HomeTemplateRealm {
    val itemType = this@mapToHomeTemplateRealm.type.orEmpty()
    val itemPosition = this@mapToHomeTemplateRealm.position.orZero()
    return HomeTemplateRealm().apply {
        position = itemPosition
        type = itemType
        labelId = this@mapToHomeTemplateRealm.label?.id.orEmpty()
        labelEn = this@mapToHomeTemplateRealm.label?.en.orEmpty()
        textId = this@mapToHomeTemplateRealm.text?.id.orEmpty()
        textEn = this@mapToHomeTemplateRealm.text?.en.orEmpty()
        images = this@mapToHomeTemplateRealm.images.orEmpty()
        links = this@mapToHomeTemplateRealm.links.orEmpty()
    }
}

internal suspend fun List<HomeTemplateRealm>.mapToModel(): List<HomeTemplate> =
    withContext(Dispatchers.IO) {
        val appRepository = KoinPlatform.getKoin().get<AppRepository>()
        val prayerRepository = KoinPlatform.getKoin().get<PrayerRepository>()
        val quranRepository = KoinPlatform.getKoin().get<QuranRepository>()
        val language = appRepository.getSetting().language

        val templates = mutableListOf<HomeTemplate>()
        var dhikrType: DhikrType? = null

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
                        prayerRepository
                            .fetchTodayTomorrowPrayerTimes()
                            .collectLatest { times ->
                                if (times.data.isEmpty()) {
                                    templates.add(
                                        HomeTemplate.PrayerTime(
                                            position = position,
                                            type = type,
                                            label = label,
                                            date = AppString.PRAYER_ENABLE_GPS.getString(),
                                            locationName = "",
                                            nextPrayerName = "",
                                            nextPrayerTime = "",
                                        ),
                                    )
                                } else {
                                    val today = times.data.first()
                                    val tomorrow = times.data.last()
                                    val nextTime =
                                        today.whatNextPrayerTime(tomorrow, true)

                                    val dayName =
                                        when (appRepository.getSetting().language) {
                                            AppSetting.Language.ENGLISH -> today.day.dayNameEn
                                            AppSetting.Language.INDONESIAN -> today.day.dayNameId
                                        }

                                    templates.add(
                                        HomeTemplate.PrayerTime(
                                            position = position,
                                            type = type,
                                            label = label,
                                            date = "$dayName, ${today.hijri.fullDate} / ${today.gregorian.fullDate}",
                                            locationName = today.locationName,
                                            nextPrayerName = nextTime.first.title.getString(),
                                            nextPrayerTime = nextTime.second,
                                        ),
                                    )

                                    when (nextTime.first) {
                                        Salah.IMSAK,
                                        Salah.SUBUH,
                                        -> {
                                            dhikrType = DhikrType.SLEEP
                                        }

                                        Salah.SYURUQ,
                                        Salah.ZHUHUR,
                                        -> {
                                            dhikrType = DhikrType.MORNING
                                        }

                                        Salah.ASHAR -> {
                                        }

                                        Salah.MAGHRIB,
                                        Salah.ISYA,
                                        -> {
                                            dhikrType = DhikrType.AFTERNOON
                                        }
                                    }
                                }
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
                                    text = text,
                                ),
                            )
                        }
                    }

                    TemplateType.LAST_READ -> {
                        val lastRead = quranRepository.getLastRead()

                        if (quranRepository.isVerseDownloaded(lastRead.verseId)) {
                            val verse = quranRepository.getVerseById(lastRead.verseId)

                            val chapterId = lastRead.chapterId
                            val verseNumber = lastRead.verseNumber
                            val textArabic = verse.textArabic

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
                    }

                    TemplateType.QURAN_VERSE -> {
                        val quranVerse: Verse?

                        if (links.isNotEmpty()) {
                            val chapterId = links[0].toInt()
                            val verseNumber = links[1].toInt()
                            val verseId = links[2].toInt()
                            val isRandom = links.getOrElse(3) { "false" } == "true"

                            quranVerse =
                                if (!isRandom && quranRepository.isVerseDownloaded(verseId)) {
                                    quranRepository.getVerseById(verseId)
                                } else {
                                    quranRepository.getRandomVerse()
                                }
                        } else {
                            quranVerse = quranRepository.getRandomVerse()
                        }

                        quranVerse.let { verse ->
                            templates.add(
                                HomeTemplate.QuranVerse(
                                    position = position,
                                    type = type,
                                    label = label,
                                    translation = verse.textTranslation,
                                    chapterNumber = verse.chapterId,
                                    verseNumber = verse.verseNumber,
                                ),
                            )
                        }
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
