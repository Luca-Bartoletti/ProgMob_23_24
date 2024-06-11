package com.example.betterpath.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedText(targetValue: Float = 0.5f) {
    val animatedValue1  = remember { Animatable(0f) }
    val durationMillis = 3000  // Durata dell'animazione in millisecondi

    LaunchedEffect(Unit) {
        animatedValue1.animateTo(
            targetValue = targetValue,
            animationSpec = tween(durationMillis)
        )
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        text = animatedValue1.value.toInt().toString() + "%",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineLarge
    )

}