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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import getPlatform
import kotlinx.serialization.Serializable

@Serializable
data class YoutubeNav(
    val videoId: String,
    val title: String = "",
)

/**
 * https://developers.google.com/youtube/iframe_api_reference#Getting_Started
 */
@Composable
fun YoutubeScreen(youtubeNav: YoutubeNav) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            val html =
                """
                <!DOCTYPE html>
                <html>
                
                    <style>
                        body, html {
                          margin: 0;
                          padding: 0;
                          width: 100%;
                          height: 100%;
                          overflow: hidden; /* Optional: Prevent scrolling */
                        }
                        
                        #player {
                          width: 100%;
                          height: 100%;
                        }
                    </style>
                                      
                    <body>
                    <div id="player"></div>
                
                    <script>
                    var tag = document.createElement('script');
                
                    tag.src = "https://www.youtube.com/iframe_api";
                    var firstScriptTag = document.getElementsByTagName('script')[0];
                    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
                
                    var player;
                    function onYouTubeIframeAPIReady() {
                        player = new YT.Player('player', {
                       
                        videoId: '${youtubeNav.videoId}',
                        playerVars: {
                        'autoplay': 1,
                        'playsinline': 1,
                        'fs': 0
                    },
                        events: {
                        'onReady': onPlayerReady,
                        'onStateChange': onPlayerStateChange
                    }
                    });
                    }
                
                    function onPlayerReady(event) {
                        event.target.playVideo();
                    }
                
                    function onPlayerStateChange(event) {
                    }
                    function stopVideo() {
                        player.stopVideo();
                    }
                    </script>
                    </body>
                </html>
                """.trimIndent()
            val state =
                rememberWebViewStateWithHTMLData(
                    data = html,
                )

            LaunchedEffect(currentCompositeKeyHash) {
                state.webSettings.apply {
                    isJavaScriptEnabled = true
                    customUserAgentString = getPlatform().name
                    androidWebSettings.domStorageEnabled = true
                    androidWebSettings.safeBrowsingEnabled = true
                }
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
