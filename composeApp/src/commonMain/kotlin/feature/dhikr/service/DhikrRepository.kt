package feature.dhikr.service

import core.data.ApiResponse
import core.data.DataState
import data.AppDatabase
import feature.dhikr.service.mapper.mapToModel
import feature.dhikr.service.mapper.mapToRealm
import feature.dhikr.service.model.Dhikr
import feature.dhikr.service.model.DhikrType
import feature.dhikr.service.source.remote.DhikrRemote
import feature.other.service.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DhikrRepository(
    private val appRepository: AppRepository,
    private val remote: DhikrRemote,
    private val database: AppDatabase,
) {
    fun fetchDhikr(dhikrType: DhikrType) =
        flow {
            val setting = appRepository.getSetting()
            val localDhikrs = database.dhikrDao().loadAllByType(listOf(dhikrType.name))

            if (localDhikrs.isEmpty()) {
                when (val result = remote.fetchDhikr(dhikrType)) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteDhikr = result.body

                        database.dhikrDao().insert(remoteDhikr.map { it.mapToRealm(dhikrType) })

                        val dhikrs =
                            database
                                .dhikrDao()
                                .loadAllByType(listOf(dhikrType.name))
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.id }
                        emit(DataState.Success(dhikrs))
                    }
                }
            } else {
                val dhikrs =
                    database
                        .dhikrDao()
                        .loadAllByType(listOf(dhikrType.name))
                        .map { it.mapToModel(setting) }
                        .sortedBy { it.id }
                emit(DataState.Success(dhikrs))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun updateDhikrCount(
        dhikrType: DhikrType,
        dhikr: Dhikr,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val current =
                database
                    .dhikrDao()
                    .loadAllByType(listOf(dhikrType.name))
                    .first { it.id == dhikr.id }
            val updated =
                current.copy(
                    count = current.count + 1,
                )
            database.dhikrDao().update(updated)
        }
    }

    fun resetDhikrCount(dhikrType: DhikrType) {
        CoroutineScope(Dispatchers.IO).launch {
            val current =
                database
                    .dhikrDao()
                    .loadAllByType(listOf(dhikrType.name))
            val updated =
                current.map {
                    it.copy(
                        count = 0,
                    )
                }
            database.dhikrDao().update(updated)
        }
    }

    fun fetchDuaCategories(tagFilter: String = "") =
        flow {
            val setting = appRepository.getSetting()
            val localDuaCategories =
                database
                    .duaCategoryDao()
                    .getAll()

            if (localDuaCategories.isEmpty()) {
                when (val result = remote.fetchDuaCategories()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        database.duaCategoryDao().insert(remoteResult.map { it.mapToRealm() })

                        val latestCategories =
                            database
                                .duaCategoryDao()
                                .getAll()
                                .filter { it.tag == tagFilter }
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.title }

                        emit(DataState.Success(latestCategories))
                    }
                }
            } else {
                val latestCategories =
                    database
                        .duaCategoryDao()
                        .getAll()
                        .filter { it.tag == tagFilter }
                        .map { it.mapToModel(setting) }
                        .sortedBy { it.title }

                emit(DataState.Success(latestCategories))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun fetchDuaByTag(tag: String) =
        flow {
            val setting = appRepository.getSetting()
            val localDuas = database.duaDao().getAll()

            if (localDuas.isEmpty()) {
                when (val result = remote.fetchDuaSunnah()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        database.duaDao().insert(remoteResult.map { it.mapToRealm() })

                        val latestDuas =
                            database
                                .duaDao()
                                .getAll()
                                .filter { it.tag.contains(tag) }
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.title }

                        emit(DataState.Success(latestDuas))
                    }
                }
            } else {
                val latestDuas =
                    database
                        .duaDao()
                        .getAll()
                        .filter { it.tag.contains(tag) }
                        .map { it.mapToModel(setting) }
                        .sortedBy { it.title }

                emit(DataState.Success(latestDuas))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun fetchAsmaulHusna() =
        flow {
            val setting = appRepository.getSetting()
            val localAsmaul = database.asmaulHusnaDao().getAll()

            if (localAsmaul.isEmpty()) {
                when (val result = remote.fetchAsmaulHusna()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        database.asmaulHusnaDao().insert(remoteResult.map { it.mapToRealm() })

                        val latestAsmaul =
                            database
                                .asmaulHusnaDao()
                                .getAll()
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.id }

                        emit(DataState.Success(latestAsmaul))
                    }
                }
            } else {
                val latestAsmaul =
                    database
                        .asmaulHusnaDao()
                        .getAll()
                        .map { it.mapToModel(setting) }
                        .sortedBy { it.id }

                emit(DataState.Success(latestAsmaul))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)
}
