package di

import core.data.NetworkSource
import feature.conversation.service.entity.ConversationRealm
import feature.dhikr.service.entity.AsmaulHusnaRealm
import feature.dhikr.service.entity.DhikrRealm
import feature.dhikr.service.entity.DuaCategoryRealm
import feature.dhikr.service.entity.DuaRealm
import feature.home.service.entity.HomeTemplateRealm
import feature.other.service.entity.AppSettingRealm
import feature.prayertime.service.entity.GuidanceRealm
import feature.prayertime.service.entity.PrayerTimeRealm
import feature.quran.service.entity.ChapterRealm
import feature.quran.service.entity.JuzRealm
import feature.quran.service.entity.LastReadRealm
import feature.quran.service.entity.PageRealm
import feature.quran.service.entity.VerseFavoriteRealm
import feature.quran.service.entity.VerseRealm
import feature.study.service.entity.NoteRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.dsl.module

val localModule =
    module {
        single { createRealm() }
    }

private fun createRealm(): Realm =
    Realm.open(
        RealmConfiguration
            .Builder(
                schema =
                    setOf(
                        AppSettingRealm::class,
                        AsmaulHusnaRealm::class,
                        ChapterRealm::class,
                        ConversationRealm::class,
                        DhikrRealm::class,
                        DuaCategoryRealm::class,
                        DuaRealm::class,
                        GuidanceRealm::class,
                        HomeTemplateRealm::class,
                        JuzRealm::class,
                        NoteRealm::class,
                        PageRealm::class,
                        PrayerTimeRealm::class,
                        VerseRealm::class,
                        VerseFavoriteRealm::class,
                        LastReadRealm::class,
                    ),
            ).name(NetworkSource.Realm.REALM_CONFIG)
            .deleteRealmIfMigrationNeeded()
            .build(),
    )
