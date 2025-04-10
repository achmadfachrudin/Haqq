package feature.web.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import core.ui.component.BaseTopAppBar
import getPlatform
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.app_name
import haqq.composeapp.generated.resources.copy
import haqq.composeapp.generated.resources.url_copied
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import openExternalLink
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Serializable
data class WebNav(
    val url: String,
    val title: String = "",
    val openExternalIOS: Boolean = false,
    val openExternalAndroid: Boolean = false,
)

@Composable
fun WebScreen(
    webNav: WebNav,
    onBackClick: () -> Unit,
) {
    when {
        getPlatform().isIOS && webNav.openExternalIOS -> {
            openExternalLink(webNav.url)
            onBackClick()
        }
        getPlatform().isAndroid && webNav.openExternalAndroid -> {
            openExternalLink(webNav.url)
            onBackClick()
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            val urlCopiedText = stringResource(Res.string.url_copied)
            BaseTopAppBar(
                title = webNav.title.ifEmpty { stringResource(Res.string.app_name) },
                showRightButton = true,
                rightButtonImage = painterResource(Res.drawable.copy),
                onLeftButtonClick = { onBackClick() },
                onRightButtonClick = {
                    clipboardManager.setText(AnnotatedString(webNav.url))
                    scope.launch {
                        snackbarHostState.showSnackbar(urlCopiedText)
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            val state = rememberWebViewState(webNav.url)

            state.webSettings.apply {
                isJavaScriptEnabled = true
                customUserAgentString = getPlatform().name
                androidWebSettings.domStorageEnabled = true
                androidWebSettings.safeBrowsingEnabled = true
            }

            when (val loadingState = state.loadingState) {
                LoadingState.Finished -> {
                }

                LoadingState.Initializing -> {
                    LinearProgressIndicator(
                        progress = { 20f },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                is LoadingState.Loading -> {
                    LinearProgressIndicator(
                        progress = { loadingState.progress },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            WebView(
                modifier = Modifier.fillMaxSize(),
                state = state,
            )
        }
    }
}
