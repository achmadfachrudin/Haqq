package core.ui.component

import androidx.compose.animation.core.tween
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun BaseImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String = "Haqq Image",
) {
    KamelImage(
        resource = asyncPainterResource(data = imageUrl),
        modifier = modifier,
        contentScale = contentScale,
        contentDescription = contentDescription,
        onLoading = { CircularProgressIndicator() },
        animationSpec = tween(),
    )
}
