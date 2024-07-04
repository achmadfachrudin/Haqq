package feature.other.service.source.remote

import core.data.ApiResponse
import core.data.safeRequest
import feature.other.service.entity.SettingEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class SettingRemoteImp(
    private val supabaseHttpClient: HttpClient,
) : SettingRemote {
    override suspend fun fetchSetting(): ApiResponse<List<SettingEntity>> =
        supabaseHttpClient.safeRequest {
            method = HttpMethod.Get
            url("setting")
        }
}
