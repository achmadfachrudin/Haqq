package feature.charity.di

import core.data.NetworkSource
import feature.charity.screen.CharityListScreenModel
import feature.charity.service.CharityRepository
import feature.charity.service.resource.remote.CharityRemote
import feature.charity.service.resource.remote.CharityRemoteImp
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val charityFeatureModule =
    module {
        factory { CharityListScreenModel(get()) }

        single<CharityRemote> { CharityRemoteImp(get(named(NetworkSource.SUPABASE))) }

        singleOf(::CharityRepository)
    }
