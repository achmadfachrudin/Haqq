package feature.home.service

import core.data.ApiResponse
import core.data.DataState
import data.AppDatabase
import feature.home.service.mapper.mapToModel
import feature.home.service.mapper.mapToRealm
import feature.home.service.resource.remote.HomeRemote
import feature.other.service.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn

class HomeRepository(
    private val appRepository: AppRepository,
    private val remote: HomeRemote,
    private val database: AppDatabase,
) {
    fun fetchHomeTemplates() =
        flow {
            val lastUpdate = appRepository.getSetting().lastUpdate
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

            val localTemplates = database.homeTemplateDao().getAll()

            val shouldUpdate =
                lastUpdate.daysUntil(today) > 7 ||
                    localTemplates.isEmpty() ||
                    localTemplates.any { it.type == "" }

            if (shouldUpdate) {
                database.homeTemplateDao().deleteAll()

                when (val result = remote.fetchHomeTemplates()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        database.homeTemplateDao().insert(remoteResult.map { it.mapToRealm() })

                        appRepository.updateLastUpdate(today)

                        val latestTemplates =
                            database
                                .homeTemplateDao()
                                .getAll()
                                .mapToModel()
                                .sortedBy { it.position }

                        emit(DataState.Success(latestTemplates))
                    }
                }
            } else {
                val latestTemplates =
                    database
                        .homeTemplateDao()
                        .getAll()
                        .mapToModel()
                        .sortedBy { it.position }

                emit(DataState.Success(latestTemplates))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)
}
