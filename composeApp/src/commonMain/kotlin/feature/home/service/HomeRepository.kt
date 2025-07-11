package feature.home.service

import AppConstant.DEFAULT_VERSE_ID
import core.data.ApiResponse
import core.data.DataState
import data.AppDatabase
import feature.dhikr.service.model.DhikrType
import feature.home.service.mapper.mapToModel
import feature.home.service.mapper.mapToRoom
import feature.home.service.model.TemplateType
import feature.home.service.resource.remote.HomeRemote
import feature.other.service.AppRepository
import feature.prayertime.service.PrayerRepository
import feature.prayertime.service.mapper.DEFAULT_TIME
import feature.prayertime.service.model.PrayerTime
import feature.prayertime.service.model.Salah
import feature.quran.service.QuranRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class HomeRepository(
    private val appRepository: AppRepository,
    private val quranRepository: QuranRepository,
    private val prayerRepository: PrayerRepository,
    private val remote: HomeRemote,
    private val database: AppDatabase,
) {
    @OptIn(ExperimentalTime::class)
    fun fetchHomeTemplates() =
        flow {
            val lastUpdate = appRepository.getSetting().lastUpdate
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

            val localTemplates = database.homeTemplateDao().getAll()

            val shouldUpdate =
                lastUpdate.daysUntil(today) > 7 ||
                    localTemplates.isEmpty() ||
                    localTemplates.any { it.type == "" }

            if (shouldUpdate) {
                database.homeTemplateDao().deleteAll()

                when (val result = remote.fetchHomeTemplates()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        // update last update date
                        appRepository.updateLastUpdate(today)

                        // save to local database
                        val remoteResult = result.body
                        database.homeTemplateDao().insert(remoteResult.map { it.mapToRoom() })
                    }
                }
            }

            // get last read verse
            val lastRead = quranRepository.getLastRead()
            val lastReadVerse = quranRepository.getVerseById(lastRead.verseId)

            // get random verse
            val randomVerse = quranRepository.getRandomVerse()

            // get quran verse
            val quranVerseLinks =
                database
                    .homeTemplateDao()
                    .getByType(listOf(TemplateType.QURAN_VERSE.name))
                    .first()
                    .links
                    .takeIf { links -> links.isNotEmpty() }
                    ?.split(",") ?: listOf()
            val quranVerse =
                if (quranVerseLinks.isNotEmpty()) {
                    val quranVerseId =
                        quranVerseLinks[2].toIntOrNull() ?: DEFAULT_VERSE_ID
                    val quranVerseIsRandom =
                        quranVerseLinks.getOrElse(3) { "false" } == "true"

                    if (!quranVerseIsRandom &&
                        quranRepository.isVerseDownloaded(
                            quranVerseId,
                        )
                    ) {
                        quranRepository.getVerseById(quranVerseId)
                    } else {
                        randomVerse
                    }
                } else {
                    randomVerse
                }

            var prayerTimeToday: PrayerTime? = null
            var prayerTimeNext = Triple(Salah.SUBUH, DEFAULT_TIME, true)
            var dhikrType: DhikrType? = null

            val todayAndTomorrow =
                prayerRepository
                    .fetchTodayTomorrowPrayerTimes()
                    .lastOrNull()
                    ?.data
                    .orEmpty()

            if (todayAndTomorrow.isNotEmpty()) {
                prayerTimeToday = todayAndTomorrow.first()
                val prayerTimeTomorrow = todayAndTomorrow.last()
                prayerTimeNext =
                    prayerTimeToday.whatNextPrayerTime(prayerTimeTomorrow, true)

                when (prayerTimeNext.first) {
                    Salah.IMSAK, Salah.SUBUH -> {
                        dhikrType = DhikrType.SLEEP
                    }

                    Salah.SYURUQ, Salah.ZHUHUR -> {
                        dhikrType = DhikrType.MORNING
                    }

                    Salah.ASHAR -> {
                    }

                    Salah.MAGHRIB, Salah.ISYA -> {
                        dhikrType = DhikrType.AFTERNOON
                    }

                    Salah.LASTTHIRD -> {
                        dhikrType = DhikrType.SLEEP
                    }
                }
            }

            val latestTemplates =
                database
                    .homeTemplateDao()
                    .getAll()
                    .mapToModel(
                        lastReadVerse = lastReadVerse,
                        quranVerse = quranVerse,
                        prayerTimeToday = prayerTimeToday,
                        prayerTimeNext = prayerTimeNext,
                        dhikrType = dhikrType,
                    )

            emit(DataState.Success(latestTemplates))
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)
}
