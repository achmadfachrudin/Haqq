package feature.home.service

import core.data.ApiResponse
import core.data.DataState
import feature.home.service.entity.HomeTemplateRealm
import feature.home.service.mapper.mapToHomeTemplateRealm
import feature.home.service.mapper.mapToModel
import feature.home.service.resource.remote.HomeRemote
import feature.other.service.AppRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
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
    private val realm: Realm,
    private val appRepository: AppRepository,
    private val remote: HomeRemote,
) {
    fun fetchHomeTemplates() =
        flow {
            val lastUpdate = appRepository.getSetting().lastUpdate
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

            val localTemplates =
                realm.query<HomeTemplateRealm>().find()

            val shouldUpdate =
                lastUpdate.daysUntil(today) > 7 ||
                    localTemplates.isEmpty() ||
                    localTemplates.any { it.type == "" }

            if (shouldUpdate) {
                realm.writeBlocking {
                    delete(HomeTemplateRealm::class)
                }

                when (val result = remote.fetchHomeTemplates()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        realm.writeBlocking {
                            remoteResult.forEach { template ->
                                copyToRealm(template.mapToHomeTemplateRealm())
                            }
                        }

                        appRepository.updateLastUpdate(today)

                        val latestTemplates =
                            realm
                                .query<HomeTemplateRealm>()
                                .find()
                                .mapToModel()
                                .sortedBy { it.position }

                        emit(DataState.Success(latestTemplates))
                    }
                }
            } else {
                val latestTemplates =
                    realm
                        .query<HomeTemplateRealm>()
                        .find()
                        .mapToModel()

                emit(DataState.Success(latestTemplates))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)
}
