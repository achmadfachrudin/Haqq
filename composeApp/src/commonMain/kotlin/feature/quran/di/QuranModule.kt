package feature.quran.di

import core.data.NetworkSource
import feature.quran.screen.VerseListScreenModel
import feature.quran.service.QuranRepository
import feature.quran.service.source.remote.QuranRemote
import feature.quran.service.source.remote.QuranRemoteImp
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val quranFeatureModule =
    module {
        viewModelOf(::VerseListScreenModel)

        single<QuranRemote> {
            QuranRemoteImp(
                get(named(NetworkSource.QURAN)),
            )
        }

        singleOf(::QuranRepository)
    }
