import di.networkModule
import feature.article.di.articleFeatureModule
import feature.charity.di.charityFeatureModule
import feature.conversation.di.conversationFeatureModule
import feature.dhikr.di.dhikrFeatureModule
import feature.home.di.homeFeatureModule
import feature.other.di.otherFeatureModule
import feature.prayertime.di.prayerFeatureModule
import feature.quran.di.quranFeatureModule
import feature.study.di.studyFeatureModule
import feature.zakat.di.zakatFeatureModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            platformModule,
            networkModule,
            articleFeatureModule,
            charityFeatureModule,
            conversationFeatureModule,
            dhikrFeatureModule,
            homeFeatureModule,
            prayerFeatureModule,
            otherFeatureModule,
            studyFeatureModule,
            quranFeatureModule,
            zakatFeatureModule,
        )
    }

// for iOS
fun initKoin() = initKoin {}
