package core.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import haqq.composeapp.generated.resources.Res
import haqq.composeapp.generated.resources.x
import org.jetbrains.compose.resources.painterResource

@Composable
fun BaseOutlineTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    prefix: String = "",
    label: String = "",
    trailingClick: () -> Unit,
    supportingText: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        prefix = {
            BaseText(prefix)
        },
        label = {
            BaseText(label)
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = { trailingClick() },
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.x),
                        contentDescription = "",
                    )
                }
            }
        },
        supportingText = supportingText,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
    )
}
