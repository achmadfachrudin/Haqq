import data.AppDatabase
import org.koin.dsl.module
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String
        get() = UIDevice.currentDevice.systemName

    override val osVersion: String
        get() = "${UIDevice.currentDevice.systemName} ${UIDevice.currentDevice.systemVersion}"

    override val device: String
        get() = UIDevice.currentDevice.name

    override val appVersionName: String
        get() = "1.0.18"

    override val appVersionCode: Int
        get() = 18

    override val isIOS: Boolean = true

    override val isAndroid: Boolean = false

    override val fullInformation: String
        get() =
            """
            App version: $appVersionName
            OS version: $osVersion
            Device: $device
            """.trimIndent()
}

actual fun getPlatform(): Platform = IOSPlatform()

actual val platformModule =
    module {
        single<Localization> { Localization() }
        single<AppDatabase> { createRoomDatabase() }
    }
