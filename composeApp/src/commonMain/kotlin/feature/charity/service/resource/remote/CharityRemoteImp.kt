package feature.charity.service.resource.remote

import core.data.ApiResponse
import core.data.safeRequest
import feature.charity.service.entity.CharityEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class CharityRemoteImp(
    private val httpClient: HttpClient,
) : CharityRemote {
    override suspend fun fetchCharities(): ApiResponse<List<CharityEntity>> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("charity")
        }
}
