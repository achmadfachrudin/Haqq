package feature.conversation.service

import core.data.ApiResponse
import core.data.DataState
import data.AppDatabase
import feature.conversation.service.mapper.mapToModel
import feature.conversation.service.mapper.mapToRealm
import feature.conversation.service.source.remote.ConversationRemote
import feature.other.service.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class ConversationRepository(
    private val appRepository: AppRepository,
    private val remote: ConversationRemote,
    private val database: AppDatabase,
) {
    fun fetchConversations() =
        flow {
            val setting = appRepository.getSetting()
            val localConversations = database.conversationDao().getAll()

            if (localConversations.isEmpty()) {
                when (val result = remote.fetchConversations()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        database.conversationDao().insert(remoteResult.map { it.mapToRealm() })

                        val latestConversations =
                            database
                                .conversationDao()
                                .getAll()
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.id }

                        emit(DataState.Success(latestConversations))
                    }
                }
            } else {
                val latestConversations =
                    database
                        .conversationDao()
                        .getAll()
                        .map { it.mapToModel(setting) }
                        .sortedBy { it.id }

                emit(DataState.Success(latestConversations))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)
}
