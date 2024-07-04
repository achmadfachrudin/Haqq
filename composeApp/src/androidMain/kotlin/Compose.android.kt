import AppConstant.URL_EMAIL
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

@Composable
actual fun openPage(platformPage: PlatformPage) {
    val context = LocalContext.current
    val packageName = context.packageName
    val activityName = platformPage.android
    val intent = Intent().setClassName(packageName, "$packageName.$activityName")
    startActivity(context, intent, null)
}

actual fun openExternalLink(url: String) {
}

@Composable
actual fun shareImage(
    imageUrl: String,
    caption: String,
) {
    val context = LocalContext.current
    CoroutineScope(Dispatchers.IO).launch {
        val url = URL(imageUrl)
        val bitmap = getBitmapFromURL(url)

        val newFile =
            File(
                context.cacheDir.toString() + "/haqq",
                "content_image.jpeg",
            )
        newFile.parentFile?.mkdirs()

        val out = FileOutputStream(newFile)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.close()

        val bitmapUri =
            FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                newFile,
            )

        val intent =
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, bitmapUri)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(context, shareIntent, null)
    }
}

private fun getBitmapFromURL(url: URL): Bitmap? =
    try {
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input = connection.inputStream
        BitmapFactory.decodeStream(input)
    } catch (e: Exception) {
        Napier.e { "Image creation failed" }
        null
    }

@Composable
actual fun shareText(message: String) {
    val intent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
    val shareIntent = Intent.createChooser(intent, null)
    startActivity(LocalContext.current, shareIntent, null)
}

@Composable
actual fun sendMail(message: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"))
    intent.putExtra(
        Intent.EXTRA_EMAIL,
        arrayOf(URL_EMAIL),
    )
    intent.putExtra(
        Intent.EXTRA_SUBJECT,
        "haqq-issue",
    )
    intent.putExtra(
        Intent.EXTRA_TEXT,
        AndroidPlatform().fullInformation +
            "\n-----------------------\n" +
            message,
    )
    startActivity(LocalContext.current, intent, null)
}
