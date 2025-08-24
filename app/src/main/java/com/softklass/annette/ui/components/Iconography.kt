package com.softklass.annette.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RoundedIconDisplay(
    icon: ImageVector,
    iconContainerColor: Color,
    iconContentColor: Color = Color.White,
    onClickIcon: () -> Unit = { },
) {
    FilledIconButton(
        onClick = onClickIcon,
        colors =
            IconButtonDefaults.filledIconButtonColors(
                containerColor = iconContainerColor,
                contentColor = iconContentColor,
            ),
        modifier = Modifier.size(40.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon",
            modifier = Modifier.size(20.dp),

        )
    }
}
