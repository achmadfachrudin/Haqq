package feature.conversation.service.source.remote

import core.data.ApiResponse
import core.data.safeRequest
import feature.conversation.service.entity.ConversationEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod

class ConversationRemoteImp(
    private val httpClient: HttpClient,
) : ConversationRemote {
    override suspend fun fetchConversations(): ApiResponse<List<ConversationEntity>> =
        httpClient.safeRequest {
            method = HttpMethod.Get
            url("conversation")
        }
}
