package feature.prayertime.di

import core.data.NetworkSource
import feature.prayertime.screen.CalendarScreenModel
import feature.prayertime.screen.GuidanceScreenModel
import feature.prayertime.screen.PrayerTimeDailyScreenModel
import feature.prayertime.service.PrayerRepository
import feature.prayertime.service.source.remote.PrayerRemote
import feature.prayertime.service.source.remote.PrayerRemoteImp
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val prayerFeatureModule =
    module {
        viewModelOf(::CalendarScreenModel)
        viewModelOf(::GuidanceScreenModel)
        viewModelOf(::PrayerTimeDailyScreenModel)

        single<PrayerRemote> {
            PrayerRemoteImp(
                get(named(NetworkSource.ALADHAN)),
                get(named(NetworkSource.SUPABASE)),
            )
        }

        singleOf(::PrayerRepository)
    }
