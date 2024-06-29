package feature.prayertime.service.model

import feature.other.service.model.AppString

enum class Salah(
    val title: AppString,
) {
    IMSAK(AppString.PRAYER_IMSAK),
    SUBUH(AppString.PRAYER_FAJR),
    SYURUQ(AppString.PRAYER_SUNRISE),
    ZHUHUR(AppString.PRAYER_DHUHR),
    ASHAR(AppString.PRAYER_ASR),
    MAGHRIB(AppString.PRAYER_MAGHRIB),
    ISYA(AppString.PRAYER_ISHA),
}
