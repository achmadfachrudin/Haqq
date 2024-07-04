package feature.prayertime.di

import core.data.NetworkSource
import feature.prayertime.screen.CalendarScreenModel
import feature.prayertime.screen.GuidanceScreenModel
import feature.prayertime.screen.MonthlyScreenModel
import feature.prayertime.screen.PrayerTimeScreenModel
import feature.prayertime.service.PrayerRepository
import feature.prayertime.service.source.remote.PrayerRemote
import feature.prayertime.service.source.remote.PrayerRemoteImp
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val prayerFeatureModule =
    module {
        factory { CalendarScreenModel(get()) }
        factory { PrayerTimeScreenModel(get()) }
        factory { GuidanceScreenModel(get()) }
        factory { MonthlyScreenModel() }

        single<PrayerRemote> {
            PrayerRemoteImp(
                get(named(NetworkSource.ALADHAN)),
                get(named(NetworkSource.SUPABASE)),
            )
        }

        singleOf(::PrayerRepository)
    }
