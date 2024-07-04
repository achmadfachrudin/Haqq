package feature.conversation.service

import core.data.ApiResponse
import core.data.DataState
import feature.conversation.service.entity.ConversationRealm
import feature.conversation.service.mapper.mapToModel
import feature.conversation.service.mapper.mapToRealm
import feature.conversation.service.source.remote.ConversationRemote
import feature.other.service.AppRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class ConversationRepository(
    private val appRepository: AppRepository,
    private val remote: ConversationRemote,
    private val realm: Realm,
) {
    fun fetchConversations() =
        flow {
            val setting = appRepository.getSetting()
            val localConversations = realm.query<ConversationRealm>().find()

            if (localConversations.isEmpty()) {
                when (val result = remote.fetchConversations()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        realm.writeBlocking {
                            remoteResult.forEach { conversation ->
                                copyToRealm(
                                    conversation.mapToRealm(),
                                )
                            }
                        }

                        val latestConversations =
                            realm
                                .query<ConversationRealm>()
                                .find()
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.id }

                        emit(DataState.Success(latestConversations))
                    }
                }
            } else {
                val latestConversations =
                    realm
                        .query<ConversationRealm>()
                        .find()
                        .map { it.mapToModel(setting) }
                        .sortedBy { it.id }

                emit(DataState.Success(latestConversations))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)
}
