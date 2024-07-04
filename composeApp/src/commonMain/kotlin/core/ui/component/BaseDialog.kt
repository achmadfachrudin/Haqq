package core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit,
    title: String = "",
    desc: String = "",
    shouldCustomContent: Boolean = false,
    negativeButtonText: String = "",
    positiveButtonText: String = "",
    onPositiveClicked: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {},
) {
    val shouldShowNegativeButton = negativeButtonText.isNotEmpty()
    val shouldShowPositiveButton = positiveButtonText.isNotEmpty()

    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Card {
            Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                if (title.isNotEmpty()) {
                    BaseTitle(text = title)
                }

                if (desc.isNotEmpty()) {
                    BaseSpacerVertical()
                    BaseText(text = desc)
                }

                if (shouldCustomContent) {
                    BaseSpacerVertical()
                    content()
                }

                if (shouldShowNegativeButton || shouldShowPositiveButton) {
                    BaseSpacerVertical(24)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        if (shouldShowNegativeButton) {
                            TextButton(
                                onClick = { onDismissRequest() },
                            ) {
                                Text(negativeButtonText)
                            }

                            BaseSpacerHorizontal()
                        }

                        if (shouldShowPositiveButton) {
                            TextButton(
                                onClick = { onPositiveClicked() },
                            ) {
                                Text(positiveButtonText)
                            }
                        }
                    }
                }
            }
        }
    }
}
