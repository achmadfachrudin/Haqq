interface Platform {
    val name: String
    val osVersion: String
    val device: String
    val appVersionName: String
    val appVersionCode: Int
    val isIOS: Boolean
    val isAndroid: Boolean
    val fullInformation: String
}

expect fun getPlatform(): Platform
