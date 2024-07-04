package feature.prayertime.service.model

enum class HijriMonth(
    val monthNumber: Int,
    val monthName: String,
) {
    MUHARRAM(1, "Muharram"),
    SAFAR(2, "Safar"),
    RABIUL_AWAL(3, "Rabiul Awal"),
    RABIUL_AKHIR(4, "Rabiul Akhir"),
    JUMADIL_AWAL(5, "Jumadil Awal"),
    JUMADIL_AKHIR(6, "Jumadil Akhir"),
    RAJAB(7, "Rajab"),
    SYABAN(8, "Sya'ban"),
    RAMADHAN(9, "Ramadhan"),
    SYAWWAL(10, "Syawwal"),
    DZULQAIDAH(11, "Dzulqaidah"),
    DZULHIJJAH(12, "Dzulhijjah"),
}
