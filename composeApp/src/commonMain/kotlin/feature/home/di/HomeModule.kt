package feature.home.di

import core.data.NetworkSource
import feature.home.screen.MainScreenModel
import feature.home.service.HomeRepository
import feature.home.service.resource.remote.HomeRemote
import feature.home.service.resource.remote.HomeRemoteImp
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeFeatureModule =
    module {
        factory { MainScreenModel(get(), get(), get()) }

        single<HomeRemote> { HomeRemoteImp(get(named(NetworkSource.SUPABASE))) }

        singleOf(::HomeRepository)
    }
