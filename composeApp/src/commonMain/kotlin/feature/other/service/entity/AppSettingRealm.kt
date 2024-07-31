package feature.other.service.entity

import feature.other.service.model.AppSetting
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class AppSettingRealm : RealmObject {
    @PrimaryKey
    var id = 0
    var languageName = AppSetting.Language.INDONESIAN.name
    var theme = AppSetting.Theme.LIGHT.name
    var themeColor = AppSetting.ThemeColor.GREEN.name
    var arabicStyleName = AppSetting.ArabicStyle.INDOPAK.name
    var arabicFontSize = 22
    var translationFontSize = 14
    var translationVisibility = true
    var transliterationFontSize = 14
    var transliterationVisibility = true
    var locationLatitude = 0.0
    var locationLongitude = 0.0
    var locationName = ""
    var acceptPrivacyPolicy = false
    var lastUpdate = "2024-04-04"
}
