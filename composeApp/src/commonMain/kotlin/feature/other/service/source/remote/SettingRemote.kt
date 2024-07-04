package feature.other.service.source.remote

import core.data.ApiResponse
import feature.other.service.entity.SettingEntity

interface SettingRemote {
    suspend fun fetchSetting(): ApiResponse<List<SettingEntity>>
}
