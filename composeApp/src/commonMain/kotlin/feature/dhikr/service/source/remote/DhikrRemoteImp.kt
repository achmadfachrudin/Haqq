package feature.dhikr.service.source.remote

import core.data.ApiResponse
import core.data.safeRequest
import feature.dhikr.service.entity.AsmaulHusnaEntity
import feature.dhikr.service.entity.DhikrEntity
import feature.dhikr.service.entity.DuaCategoryEntity
import feature.dhikr.service.entity.DuaEntity
import feature.dhikr.service.model.DhikrType
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class DhikrRemoteImp(
    private val httpClient: HttpClient,
) : DhikrRemote {
    override suspend fun fetchDhikr(dhikrType: DhikrType): ApiResponse<List<DhikrEntity>> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url(dhikrType.table)
        }

    override suspend fun fetchDuaCategories(): ApiResponse<List<DuaCategoryEntity>> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("dua_category")
        }

    override suspend fun fetchDuaSunnah(): ApiResponse<List<DuaEntity>> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("dua_sunnah")
        }

    override suspend fun fetchAsmaulHusna(): ApiResponse<List<AsmaulHusnaEntity>> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("asmaul_husna")
        }
}
