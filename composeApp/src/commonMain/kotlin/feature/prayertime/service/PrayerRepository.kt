package feature.prayertime.service

import core.data.ApiResponse
import core.data.DataState
import data.AppDatabase
import feature.other.service.AppRepository
import feature.prayertime.service.mapper.mapToHaqqCalendar
import feature.prayertime.service.mapper.mapToHijri
import feature.prayertime.service.mapper.mapToModel
import feature.prayertime.service.mapper.mapToRoom
import feature.prayertime.service.model.GuidanceType
import feature.prayertime.service.source.remote.PrayerRemote
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.prayer_enable_gps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

class PrayerRepository(
    private val appRepository: AppRepository,
    private val remote: PrayerRemote,
    private val database: AppDatabase,
) {
    fun fetchGuidance(guidanceType: GuidanceType) =
        flow {
            val localGuidances = database.guidanceDao().loadAllByType(listOf(guidanceType.name))

            if (localGuidances.isEmpty()) {
                when (val result = remote.fetchGuidance(guidanceType.table)) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        database.guidanceDao().insert(remoteResult.map { it.mapToRoom() })

                        val latestGuidances =
                            database
                                .guidanceDao()
                                .loadAllByType(listOf(guidanceType.name))
                                .sortedBy { it.position }
                                .map { it.mapToModel() }

                        emit(DataState.Success(latestGuidances))
                    }
                }
            } else {
                val latestGuidances =
                    database
                        .guidanceDao()
                        .loadAllByType(listOf(guidanceType.name))
                        .sortedBy { it.position }
                        .map { it.mapToModel() }

                emit(DataState.Success(latestGuidances))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun fetchConvertGregorianToHijri(
        date: Int,
        month: Int,
        year: Int,
    ) = flow {
        when (
            val result =
                remote.fetchConvertGregorianToHijri(
                    date = "$date-$month-$year",
                )
        ) {
            is ApiResponse.Error -> emit(DataState.Error(result.message))
            is ApiResponse.Success -> emit(DataState.Success(result.body.mapToHijri()))
        }
    }.onStart { emit(DataState.Loading) }
        .catch { emit(DataState.Error(it.message.orEmpty())) }
        .flowOn(Dispatchers.IO)

    fun fetchTodayTomorrowPrayerTimes() =
        flow {
            val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val tomorrow: LocalDate = today.plus(1, DateTimeUnit.DAY)
            val nextMonth: LocalDate = today.plus(1, DateTimeUnit.MONTH)
            val setting = appRepository.getSetting()

            val localPrayerTimes =
                database
                    .prayerTimeDao()
                    .getAll()
                    .filter {
                        val date = LocalDate(it.gregorianYear, it.gregorianMonth, it.gregorianDate)

                        (date == today || date == tomorrow) &&
                            it.locationName.lowercase() == setting.location.name.lowercase()
                    }.sortedBy { it.gregorianYear }
                    .map { it.mapToModel() }

            if (localPrayerTimes.size == 2) {
                emit(DataState.Success(localPrayerTimes))
            } else {
                database.prayerTimeDao().deleteAll()

                if (today.month != tomorrow.month) {
                    // get this month
                    fetchGregorianMonthTimes(
                        month = today.monthNumber,
                        year = today.year,
                    ).last()

                    // get next month
                    fetchGregorianMonthTimes(
                        month = nextMonth.monthNumber,
                        year = nextMonth.year,
                    ).last()
                } else {
                    // get this month
                    fetchGregorianMonthTimes(
                        month = today.monthNumber,
                        year = today.year,
                    ).last()
                }

                val latestPrayerTimes =
                    database
                        .prayerTimeDao()
                        .getAll()
                        .filter {
                            val date =
                                LocalDate(it.gregorianYear, it.gregorianMonth, it.gregorianDate)

                            (date == today || date == tomorrow) &&
                                it.locationName.lowercase() == setting.location.name.lowercase()
                        }.sortedBy { it.gregorianYear }
                        .map { it.mapToModel() }

                emit(DataState.Success(latestPrayerTimes))
            }
        }.flowOn(Dispatchers.IO)

    private fun fetchGregorianMonthTimes(
        month: Int,
        year: Int,
    ) = flow {
        val setting = appRepository.getSetting()

        when (
            val result =
                remote.fetchGregorianMonthTimes(
                    month = month,
                    year = year,
                    latitude = setting.location.latitude,
                    longitude = setting.location.longitude,
                )
        ) {
            is ApiResponse.Error -> {
                emit(DataState.Error(result.message))
            }

            is ApiResponse.Success -> {
                val remoteResult = result.body

                remoteResult.data?.let { calendar ->
                    database
                        .prayerTimeDao()
                        .insert(calendar.map { it.mapToRoom(setting.location.name) })
                }

                emit(DataState.Success(result.body.mapToHaqqCalendar()))
            }
        }
    }.onStart { emit(DataState.Loading) }
        .catch { emit(DataState.Error(it.message.orEmpty())) }
        .flowOn(Dispatchers.IO)

    fun fetchHijriMonthTimes(
        month: Int,
        year: Int,
    ) = flow {
        val setting = appRepository.getSetting()

        when (
            val result =
                remote.fetchHijriMonthTimes(
                    month = month,
                    year = year,
                    latitude = setting.location.latitude,
                    longitude = setting.location.longitude,
                )
        ) {
            is ApiResponse.Error -> {
                if (setting.isLocationValid) {
                    emit(DataState.Error(result.message))
                } else {
                    emit(DataState.Error(Res.string.prayer_enable_gps.key))
                }
            }

            is ApiResponse.Success -> emit(DataState.Success(result.body.mapToHaqqCalendar()))
        }
    }.onStart { emit(DataState.Loading) }
        .catch { emit(DataState.Error(it.message.orEmpty())) }
        .flowOn(Dispatchers.IO)
}
