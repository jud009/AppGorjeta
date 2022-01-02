package com.estud.tipapp.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color = Color.Black.copy(alpha = 0.75f), //75% visible
    backgroundColor: Color = Color.White,
    elevation: Dp = 4.dp,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .size(50.dp)
            .padding(all = 8.dp)
            .clickable { onClick.invoke() }
            /*.then(increaseSizeModifier)*/,
        shape = CircleShape,
        backgroundColor = backgroundColor,
        elevation = elevation,
    ) {
        Icon(imageVector = imageVector, contentDescription = "Round Icon", tint = tint)
    }
}
