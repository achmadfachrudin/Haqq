package feature.conversation.di

import core.data.NetworkSource
import feature.conversation.screen.ConversationScreenModel
import feature.conversation.service.ConversationRepository
import feature.conversation.service.source.remote.ConversationRemote
import feature.conversation.service.source.remote.ConversationRemoteImp
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val conversationFeatureModule =
    module {
        viewModelOf(::ConversationScreenModel)

        single<ConversationRemote> { ConversationRemoteImp(get(named(NetworkSource.SUPABASE))) }
        singleOf(::ConversationRepository)
    }
