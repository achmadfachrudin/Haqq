package feature.other.service.mapper

import feature.other.service.AppRepository
import feature.other.service.model.AppSetting
import feature.other.service.model.AppString
import org.koin.mp.KoinPlatform

fun AppString.getString(): String {
    val appRepository = KoinPlatform.getKoin().get<AppRepository>()

    return when (appRepository.getSetting().language) {
        AppSetting.Language.ENGLISH -> this.en
        AppSetting.Language.INDONESIAN -> this.id
    }
}
