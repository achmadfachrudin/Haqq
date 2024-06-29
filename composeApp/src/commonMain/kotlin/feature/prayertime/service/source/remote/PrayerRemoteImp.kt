package feature.prayertime.service.source.remote

import core.data.ApiResponse
import core.data.NetworkSource.Aladhan
import core.data.safeRequest
import feature.prayertime.service.entity.ConvertDateEntity
import feature.prayertime.service.entity.GuidanceEntity
import feature.prayertime.service.entity.HaqqCalendarEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class PrayerRemoteImp(
    private val aladhanHttpClient: HttpClient,
    private val supabaseHttpClient: HttpClient,
) : PrayerRemote {
    override suspend fun fetchGuidance(tableName: String): ApiResponse<List<GuidanceEntity>> =
        supabaseHttpClient.safeRequest {
            method = HttpMethod.Get
            url(tableName)
        }

    /**
     * Fetch Convert Gregorian to Hijri
     * @param date in Gregorian ex: 07-12-2014
     */
    override suspend fun fetchConvertGregorianToHijri(date: String): ApiResponse<ConvertDateEntity> =
        aladhanHttpClient.safeRequest {
            method = HttpMethod.Get
            url("gToH/$date")
        }

    /**
     * Fetch Gregorian Month
     * @param month in Gregorian ex: 12
     * @param year in Gregorian ex: 2023
     */
    override suspend fun fetchGregorianMonthTimes(
        month: Int,
        year: Int,
        latitude: Double,
        longitude: Double,
    ): ApiResponse<HaqqCalendarEntity> =
        aladhanHttpClient.safeRequest {
            method = HttpMethod.Get
            url("calendar/$year/$month")
            parameter(Aladhan.PARAM_LATITUDE, latitude)
            parameter(Aladhan.PARAM_LONGITUDE, longitude)
        }

    /**
     * Fetch Hijri Month
     * @param month in Hijri ex: 12
     * @param year in Hijri ex: 1440
     */
    override suspend fun fetchHijriMonthTimes(
        month: Int,
        year: Int,
        latitude: Double,
        longitude: Double,
    ): ApiResponse<HaqqCalendarEntity> =
        aladhanHttpClient.safeRequest {
            method = HttpMethod.Get
            url("hijriCalendar/$year/$month")
            parameter(Aladhan.PARAM_LATITUDE, latitude)
            parameter(Aladhan.PARAM_LONGITUDE, longitude)
        }
}
