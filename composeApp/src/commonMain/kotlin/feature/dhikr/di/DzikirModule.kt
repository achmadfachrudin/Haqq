package feature.dhikr.di

import core.data.NetworkSource
import feature.dhikr.screen.AsmaulHusnaScreenModel
import feature.dhikr.screen.DhikrListScreenModel
import feature.dhikr.screen.DuaListScreenModel
import feature.dhikr.screen.DuaSunnahScreenModel
import feature.dhikr.service.DhikrRepository
import feature.dhikr.service.source.remote.DhikrRemote
import feature.dhikr.service.source.remote.DhikrRemoteImp
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dhikrFeatureModule =
    module {
        viewModelOf(::DhikrListScreenModel)
        viewModelOf(::DuaSunnahScreenModel)
        viewModelOf(::DuaListScreenModel)
        viewModelOf(::AsmaulHusnaScreenModel)

        single<DhikrRemote> { DhikrRemoteImp(get(named(NetworkSource.SUPABASE))) }
        singleOf(::DhikrRepository)
    }
