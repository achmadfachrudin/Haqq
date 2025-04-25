package feature.other.service.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import feature.other.service.model.AppSetting

@Entity
data class AppSettingRealm(
    @PrimaryKey
    var id: Int = 0,
    var languageName: String = AppSetting.Language.INDONESIAN.name,
    var theme: String = AppSetting.Theme.LIGHT.name,
    var themeColor: String = AppSetting.ThemeColor.GREEN.name,
    var arabicStyleName: String = AppSetting.ArabicStyle.INDOPAK.name,
    var arabicFontSize: Int = 22,
    var translationFontSize: Int = 14,
    var translationVisibility: Boolean = true,
    var transliterationFontSize: Int = 14,
    var transliterationVisibility: Boolean = true,
    var locationLatitude: Double = 0.0,
    var locationLongitude: Double = 0.0,
    var locationName: String = "",
    var acceptPrivacyPolicy: Boolean = false,
    var lastUpdate: String = "2024-04-04",
)
