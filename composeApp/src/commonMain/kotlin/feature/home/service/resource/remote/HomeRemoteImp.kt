package feature.home.service.resource.remote

import core.data.ApiResponse
import core.data.safeRequest
import feature.home.service.entity.HomeTemplateEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class HomeRemoteImp(
    private val httpClient: HttpClient,
) : HomeRemote {
    override suspend fun fetchHomeTemplates(): ApiResponse<List<HomeTemplateEntity>> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("home_template")
        }
}
