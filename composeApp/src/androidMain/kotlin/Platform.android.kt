import android.os.Build

class AndroidPlatform : Platform {
    override val name: String
        get() = "Android"

    override val osVersion: String
        get() = "Android ${Build.VERSION.RELEASE} SDK ${Build.VERSION.SDK_INT}"

    override val device: String
        get() = "${Build.MANUFACTURER} - ${Build.MODEL} - ${Build.BRAND}"

    override val appVersionName: String
        get() = "1.0.15"

    override val appVersionCode: Int
        get() = 15

    override val isIOS: Boolean
        get() = false

    override val isAndroid: Boolean
        get() = true

    override val fullInformation: String
        get() =
            """
            App version: $appVersionName
            OS version: $osVersion
            Device: $device
            """.trimIndent()
}

actual fun getPlatform(): Platform = AndroidPlatform()
