package feature.prayertime.service

import core.data.ApiResponse
import core.data.DataState
import feature.other.service.AppRepository
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import feature.prayertime.service.entity.GuidanceRealm
import feature.prayertime.service.entity.PrayerTimeRealm
import feature.prayertime.service.mapper.mapToGuidanceRealm
import feature.prayertime.service.mapper.mapToHaqqCalendar
import feature.prayertime.service.mapper.mapToHijri
import feature.prayertime.service.mapper.mapToModel
import feature.prayertime.service.mapper.mapToPrayerTimeRealm
import feature.prayertime.service.model.GuidanceType
import feature.prayertime.service.source.remote.PrayerRemote
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

class PrayerRepository(
    private val realm: Realm,
    private val appRepository: AppRepository,
    private val remote: PrayerRemote,
) {
    fun fetchGuidance(guidanceType: GuidanceType) =
        flow {
            val localGuidances =
                realm
                    .query<GuidanceRealm>()
                    .find()
                    .filter { it.type == guidanceType.name }

            if (localGuidances.isEmpty()) {
                when (val result = remote.fetchGuidance(guidanceType.table)) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        realm.writeBlocking {
                            remoteResult.forEach { guidance ->
                                copyToRealm(guidance.mapToGuidanceRealm())
                            }
                        }

                        val latestGuidances =
                            realm
                                .query<GuidanceRealm>()
                                .find()
                                .filter { it.type == guidanceType.name }
                                .sortedBy { it.position }
                                .map { it.mapToModel() }

                        emit(DataState.Success(latestGuidances))
                    }
                }
            } else {
                val latestGuidances =
                    realm
                        .query<GuidanceRealm>()
                        .find()
                        .filter { it.type == guidanceType.name }
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
            val setting = appRepository.getSetting()

            val localTodayTomorrowPrayerTimes =
                realm
                    .query<PrayerTimeRealm>()
                    .find()
                    .filter {
                        val saved = LocalDate(it.gregorianYear, it.gregorianMonth, it.gregorianDate)

                        (saved == today || saved == tomorrow) &&
                            it.locationName.lowercase() == setting.location.name.lowercase()
                    }.sortedBy { it.gregorianFullDate }
                    .map { it.mapToModel() }

            if (localTodayTomorrowPrayerTimes.size == 2) {
                emit(DataState.Success(localTodayTomorrowPrayerTimes))
            } else {
                if (!setting.isLocationValid) {
                    emit(DataState.Error(AppString.PRAYER_ENABLE_GPS.getString()))
                    return@flow
                }

                realm.writeBlocking {
                    delete(PrayerTimeRealm::class)
                }

                when (
                    val result =
                        remote.fetchGregorianMonthTimes(
                            month = today.monthNumber,
                            year = today.year,
                            latitude = setting.location.latitude,
                            longitude = setting.location.longitude,
                        )
                ) {
                    is ApiResponse.Error -> {
                        if (setting.isLocationValid) {
                            emit(DataState.Error(result.message))
                        } else {
                            emit(DataState.Error(AppString.PRAYER_ENABLE_GPS.getString()))
                        }
                    }

                    is ApiResponse.Success -> {
                        realm.writeBlocking {
                            result.body.data?.forEach { day ->
                                copyToRealm(
                                    day.mapToPrayerTimeRealm(setting.location.name),
                                )
                            }
                        }

                        val todayTomorrowPrayerTimes =
                            realm
                                .query<PrayerTimeRealm>()
                                .find()
                                .filter {
                                    val saved =
                                        LocalDate(
                                            it.gregorianYear,
                                            it.gregorianMonth,
                                            it.gregorianDate,
                                        )

                                    (saved == today || saved == tomorrow) &&
                                        it.locationName.lowercase() == setting.location.name.lowercase()
                                }.sortedBy { it.gregorianFullDate }
                                .map { it.mapToModel() }

                        emit(DataState.Success(todayTomorrowPrayerTimes))
                    }
                }
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    private fun fetchGregorianMonthTimes(
        month: Int,
        year: Int,
    ) = flow {
        val setting = appRepository.getSetting()

        realm.writeBlocking {
            delete(PrayerTimeRealm::class)
        }

        when (
            val result =
                remote.fetchGregorianMonthTimes(
                    month = month,
                    year = year,
                    latitude = setting.location.latitude,
                    longitude = setting.location.longitude,
                )
        ) {
            is ApiResponse.Error -> emit(DataState.Error(result.message))
            is ApiResponse.Success -> {
                realm.writeBlocking {
                    result.body.data?.forEach { day ->
                        copyToRealm(
                            day.mapToPrayerTimeRealm(setting.location.name),
                        )
                    }
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
                    emit(DataState.Error(AppString.PRAYER_ENABLE_GPS.getString()))
                }
            }

            is ApiResponse.Success -> emit(DataState.Success(result.body.mapToHaqqCalendar()))
        }
    }.onStart { emit(DataState.Loading) }
        .catch { emit(DataState.Error(it.message.orEmpty())) }
        .flowOn(Dispatchers.IO)
}
