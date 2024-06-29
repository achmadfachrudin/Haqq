package feature.zakat.di

import feature.zakat.screen.ZakatScreenModel
import org.koin.dsl.module

val zakatFeatureModule =
    module {
        factory { ZakatScreenModel(get()) }
    }
