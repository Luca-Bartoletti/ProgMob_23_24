package com.example.betterpath.composables

import android.animation.ValueAnimator
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun AnimatedText(targetValue: Float = 0.5f) {
    var animatedValue1  = remember { "0" }
    val durationMillis = 3000  // Durata dell'animazione in millisecondi

    val animator = ValueAnimator.ofFloat(0f, targetValue)
    animator.setDuration(durationMillis.toLong())
    animator.addUpdateListener { animation -> animatedValue1 = animation.animatedValue.toString() }
    animator.start()

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        text = "${animatedValue1.toInt()} %",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineLarge
    )
}
