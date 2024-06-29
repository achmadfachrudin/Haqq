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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import core.ui.component.BaseTopAppBar
import feature.other.service.mapper.getString
import feature.other.service.model.AppString
import getPlatform
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.copy
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

class WebScreen(
    val url: String,
    val title: String = "",
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val clipboardManager = LocalClipboardManager.current

        Scaffold(
            topBar = {
                BaseTopAppBar(
                    title = title.ifEmpty { AppString.APP_NAME.getString() },
                    showRightButton = true,
                    rightButtonImage = painterResource(Res.drawable.copy),
                    onLeftButtonClick = { navigator.pop() },
                    onRightButtonClick = {
                        clipboardManager.setText(AnnotatedString(url))
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                AppString.URL_COPIED.getString(),
                            )
                        }
                    },
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                val state = rememberWebViewState(url)

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
}