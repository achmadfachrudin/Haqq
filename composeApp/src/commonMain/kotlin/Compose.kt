import androidx.compose.runtime.Composable

enum class PlatformPage(
    val android: String,
    val iOS: String,
) {
    NONE("", ""),
    QIBLA("qibla.QiblaActivity", ""),
}

@Composable
expect fun openPage(platformPage: PlatformPage)

expect fun openExternalLink(url: String)

@Composable
expect fun shareImage(
    imageUrl: String,
    caption: String = "",
)

@Composable
expect fun shareText(message: String)

@Composable
expect fun sendMail(message: String = "")
