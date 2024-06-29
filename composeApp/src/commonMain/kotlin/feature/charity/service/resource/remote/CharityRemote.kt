package feature.charity.service.resource.remote

import core.data.ApiResponse
import feature.charity.service.entity.CharityEntity

interface CharityRemote {
    suspend fun fetchCharities(): ApiResponse<List<CharityEntity>>
}
