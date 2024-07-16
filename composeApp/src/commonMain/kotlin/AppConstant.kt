object AppConstant {
    private const val URL_WEB = "https://sites.google.com/view/haqq-app/xx/"
    private const val PATH_ABOUT = "about/"
    private const val PATH_LICENSES = "licenses/"
    private const val PATH_PRIVACY_POLICY = "privacy-policy/"

    private fun getWebUrl(languageId: String) = URL_WEB.replace("xx", languageId)
    fun getAboutUrl(languageId: String) = getWebUrl(languageId).plus(PATH_ABOUT)
    fun getLicensesUrl(languageId: String) = getWebUrl(languageId).plus(PATH_LICENSES)
    fun getPrivacyPoilicyUrl(languageId: String) = getWebUrl(languageId).plus(PATH_PRIVACY_POLICY)

    const val URL_SUPPORT = "https://sociabuzz.com/haqq/tribe/"

    const val USERNAME_INSTAGRAM = "haqq_sunnah"
    const val URL_INSTAGRAM = "https://www.instagram.com/${USERNAME_INSTAGRAM}/"
    const val URL_EMAIL = "haqqsunnah@gmail.com"

    const val DEFAULT_CHAPTER_NAME = "Al-Fatihah"
    const val DEFAULT_CHAPTER_ID = 1
    const val DEFAULT_VERSE_ID = 1
    const val DEFAULT_VERSE_NUMBER = 1

    const val DEFAULT_LOCATION_LATITUDE = 0.0
    const val DEFAULT_LOCATION_LONGITUDE = 0.0
    const val DEFAULT_LOCATION_NAME = ""
}
