import AppConstant.URL_EMAIL
import androidx.compose.runtime.Composable
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSURL.Companion.URLWithString
import platform.Foundation.create
import platform.MessageUI.MFMailComposeViewController
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage

@Composable
actual fun OpenPage(platformPage: PlatformPage) {
}

actual fun openExternalLink(url: String) {
    URLWithString(url)?.let {
        UIApplication.sharedApplication.openURL(it)
    }
}

@OptIn(BetaInteropApi::class)
@Composable
actual fun ShareImage(
    imageUrl: String,
    caption: String,
) {
    CoroutineScope(Dispatchers.IO).launch {
        val client = HttpClient()
        val response: HttpResponse = client.get(imageUrl)
        val imageBytes: ByteArray = response.readBytes()
        val imageData = imageBytes.toNSData()

        withContext(Dispatchers.Main) {
            val image = UIImage.imageWithData(data = imageData)
            image?.let {
                val activityViewController = UIActivityViewController(listOf(image), null)
                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    activityViewController,
                    true,
                    null,
                )
            } ?: Napier.e { "Image creation failed" }
        }
    }
}

@BetaInteropApi
@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData =
    memScoped {
        NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
    }

@Composable
actual fun ShareText(message: String) {
    val activityViewController = UIActivityViewController(listOf(message), null)
    UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
        activityViewController,
        true,
        null,
    )
}

@Composable
actual fun SendMail(subject: String, message: String) {
    if (MFMailComposeViewController.canSendMail()) {
        val mail = MFMailComposeViewController()
        mail.setToRecipients(listOf(URL_EMAIL))
        mail.setSubject(subject)

        mail.setMessageBody(
            body =
                IOSPlatform().fullInformation +
                    "\n-----------------------\n" +
                    message,
            isHTML = false,
        )

        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            mail,
            true,
            null,
        )
    }
}
