package feature.conversation.service.source.remote

import core.data.ApiResponse
import feature.conversation.service.entity.ConversationEntity

interface ConversationRemote {
    suspend fun fetchConversations(): ApiResponse<List<ConversationEntity>>
}
