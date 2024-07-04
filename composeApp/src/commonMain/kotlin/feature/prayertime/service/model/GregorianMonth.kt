package feature.prayertime.service.model

enum class GregorianMonth(
    val monthNumber: Int,
    val monthName: String,
) {
    JANUARY(1, "Januari"),
    FEBRUARY(2, "Februari"),
    MARCH(3, "Maret"),
    APRIL(4, "April"),
    MAY(5, "Mei"),
    JUNE(6, "Juni"),
    JULY(7, "Juli"),
    AUGUST(8, "Agustus"),
    SEPTEMBER(9, "September"),
    OCTOBER(10, "Oktober"),
    NOVEMBER(11, "November"),
    DECEMBER(12, "Desember"),
}
