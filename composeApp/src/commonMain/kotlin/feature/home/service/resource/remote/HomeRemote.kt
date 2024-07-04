package feature.home.service.resource.remote

import core.data.ApiResponse
import feature.home.service.entity.HomeTemplateEntity

interface HomeRemote {
    suspend fun fetchHomeTemplates(): ApiResponse<List<HomeTemplateEntity>>
}
