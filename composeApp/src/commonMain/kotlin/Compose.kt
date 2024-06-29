import androidx.compose.runtime.Composable

enum class PlatformPage(
    val android: String,
    val iOS: String,
) {
    NONE("", ""),
    QIBLA("qibla.QiblaActivity", ""),
}

@Composable
expect fun OpenPage(platformPage: PlatformPage)

expect fun openExternalLink(url: String)

@Composable
expect fun ShareImage(
    imageUrl: String,
    caption: String = "",
)

@Composable
expect fun ShareText(message: String)

@Composable
expect fun SendMail(subject: String, message: String)
