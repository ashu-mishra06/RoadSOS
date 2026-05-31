package com.example.roadsos.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun SmoothText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current
) {
    Crossfade(
        targetState = text,
        animationSpec = tween(
            durationMillis = 220
        ),
        label = "smoothLanguageText"
    ) { animatedText ->

        Text(
            text = animatedText,
            modifier = modifier,
            color = color,
            style = style
        )
    }
}