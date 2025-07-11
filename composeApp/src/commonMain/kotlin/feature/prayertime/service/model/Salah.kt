package feature.prayertime.service.model

import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.prayer_asr
import haqq.composeapp.generated.resources.prayer_dhuhr
import haqq.composeapp.generated.resources.prayer_fajr
import haqq.composeapp.generated.resources.prayer_imsak
import haqq.composeapp.generated.resources.prayer_isha
import haqq.composeapp.generated.resources.prayer_lastthird
import haqq.composeapp.generated.resources.prayer_maghrib
import haqq.composeapp.generated.resources.prayer_sunrise
import org.jetbrains.compose.resources.StringResource

enum class Salah(
    val titleRes: StringResource,
) {
    LASTTHIRD(Res.string.prayer_lastthird),
    IMSAK(Res.string.prayer_imsak),
    SUBUH(Res.string.prayer_fajr),
    SYURUQ(Res.string.prayer_sunrise),
    ZHUHUR(Res.string.prayer_dhuhr),
    ASHAR(Res.string.prayer_asr),
    MAGHRIB(Res.string.prayer_maghrib),
    ISYA(Res.string.prayer_isha),
}
