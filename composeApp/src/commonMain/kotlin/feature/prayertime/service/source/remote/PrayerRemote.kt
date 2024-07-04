package feature.prayertime.service.source.remote

import core.data.ApiResponse
import feature.prayertime.service.entity.ConvertDateEntity
import feature.prayertime.service.entity.GuidanceEntity
import feature.prayertime.service.entity.HaqqCalendarEntity

interface PrayerRemote {
    suspend fun fetchGuidance(tableName: String): ApiResponse<List<GuidanceEntity>>

    suspend fun fetchConvertGregorianToHijri(date: String): ApiResponse<ConvertDateEntity>

    suspend fun fetchGregorianMonthTimes(
        month: Int,
        year: Int,
        latitude: Double,
        longitude: Double,
    ): ApiResponse<HaqqCalendarEntity>

    suspend fun fetchHijriMonthTimes(
        month: Int,
        year: Int,
        latitude: Double,
        longitude: Double,
    ): ApiResponse<HaqqCalendarEntity>
}
