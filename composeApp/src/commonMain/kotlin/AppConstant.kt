object AppConstant {
    private const val URL_WEB = "https://sites.google.com/view/haqq-app/"
    private const val PATH_ABOUT = "about/"
    private const val PATH_LICENSES = "licenses/"
    private const val PATH_PRIVACY_POLICY = "privacy-policy/"
    private const val PATH_SUPPORT = "support/"

    const val URL_LYNK_KIDS_ACTIVITY = "https://lynk.id/haqqsunnah/page/kids-activity/"

    const val USERNAME_INSTAGRAM = "haqq_sunnah"
    const val URL_INSTAGRAM = "https://www.instagram.com/${USERNAME_INSTAGRAM}/"
    const val URL_X = "https://www.x.com/${USERNAME_INSTAGRAM}/"
    const val URL_EMAIL = "haqqsunnah@gmail.com"

    const val DEFAULT_CHAPTER_NAME = "Al-Fatihah"
    const val DEFAULT_CHAPTER_ID = 1
    const val DEFAULT_VERSE_ID = 1
    const val DEFAULT_VERSE_NUMBER = 1
    const val DEFAULT_PROGRESS_FLOAT = 0.14f
    const val DEFAULT_PROGRESS_INT = 14

    const val DEFAULT_LOCATION_LATITUDE = 0.0
    const val DEFAULT_LOCATION_LONGITUDE = 0.0
    const val DEFAULT_LOCATION_NAME = ""

    private fun getWebUrl(languageId: String) = URL_WEB.plus("$languageId/")

    fun getAboutUrl(languageId: String) = getWebUrl(languageId).plus(PATH_ABOUT)

    fun getLicensesUrl(languageId: String) = getWebUrl(languageId).plus(PATH_LICENSES)

    fun getPrivacyPolicyUrl(languageId: String) = getWebUrl(languageId).plus(PATH_PRIVACY_POLICY)

    fun getSupportUrl(languageId: String) = getWebUrl(languageId).plus(PATH_SUPPORT)
}
