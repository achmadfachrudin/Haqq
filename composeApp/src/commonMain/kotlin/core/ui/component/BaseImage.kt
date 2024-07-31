package core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.SubcomposeAsyncImage

@Composable
fun BaseImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String = "Haqq Image",
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        modifier = modifier,
        contentScale = contentScale,
        contentDescription = contentDescription,
        loading = {
            LoadingState()
        },
    )
}
