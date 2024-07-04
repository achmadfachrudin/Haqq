package feature.prayertime.service.model

enum class HijriDay(
    val dayNumber: Int,
    val dayNameId: String,
    val shortNameId: String,
    val dayNameEn: String,
    val shortNameEn: String,
) {
    SENIN(1, "Senin", "Sen", "Monday", "Mon"),
    SELASA(2, "Selasa", "Sel", "Tuesday", "Tue"),
    RABU(3, "Rabu", "Rab", "Wednesday", "Wed"),
    KAMIS(4, "Kamis", "Kam", "Thursday", "Thu"),
    JUMAT(5, "Jum'at", "Jum", "Friday", "Fri"),
    SABTU(6, "Sabtu", "Sab", "Saturday", "Sat"),
    AHAD(7, "Ahad", "Ahad", "Ahad", "Ahad"),
}
