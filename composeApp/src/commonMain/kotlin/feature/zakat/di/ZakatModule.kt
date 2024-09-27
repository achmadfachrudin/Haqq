package feature.zakat.di

import feature.zakat.screen.ZakatScreenModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val zakatFeatureModule =
    module {
        viewModelOf(::ZakatScreenModel)
    }
