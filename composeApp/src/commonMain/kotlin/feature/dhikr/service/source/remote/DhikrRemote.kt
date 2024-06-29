package feature.dhikr.service.source.remote

import core.data.ApiResponse
import feature.dhikr.service.entity.AsmaulHusnaEntity
import feature.dhikr.service.entity.DhikrEntity
import feature.dhikr.service.entity.DuaCategoryEntity
import feature.dhikr.service.entity.DuaEntity
import feature.dhikr.service.model.DhikrType

interface DhikrRemote {
    suspend fun fetchDhikr(dhikrType: DhikrType): ApiResponse<List<DhikrEntity>>

    suspend fun fetchDuaCategories(): ApiResponse<List<DuaCategoryEntity>>

    suspend fun fetchDuaSunnah(): ApiResponse<List<DuaEntity>>

    suspend fun fetchAsmaulHusna(): ApiResponse<List<AsmaulHusnaEntity>>
}
