package feature.conversation.screen

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.data.DataState
import feature.conversation.service.ConversationRepository
import feature.conversation.service.model.Conversation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ConversationScreenModel(
    private val repository: ConversationRepository,
) : StateScreenModel<ConversationScreenModel.State>(State.Loading) {
    sealed class State {
        object Loading : State()

        data class Error(
            val message: String,
        ) : State()

        data class Content(
            val conversations: List<Conversation>,
        ) : State()
    }

    fun getConversation() {
        screenModelScope.launch {
            repository.fetchConversations().collectLatest {
                mutableState.value =
                    when (it) {
                        is DataState.Error -> State.Error(it.message)
                        DataState.Loading -> State.Loading
                        is DataState.Success -> State.Content(it.data)
                    }
            }
        }
    }
}
