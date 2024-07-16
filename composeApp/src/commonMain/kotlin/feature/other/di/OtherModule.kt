package feature.other.di

import core.data.NetworkSource
import feature.other.screen.OtherScreenModel
import feature.other.screen.SettingScreenModel
import feature.other.service.AppRepository
import feature.other.service.source.remote.SettingRemote
import feature.other.service.source.remote.SettingRemoteImp
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val otherFeatureModule =
    module {
        factory { OtherScreenModel(get()) }
        factory { SettingScreenModel(get(), get()) }

        single<SettingRemote> {
            SettingRemoteImp(
                get(named(NetworkSource.SUPABASE)),
            )
        }
        singleOf(::AppRepository)
    }
