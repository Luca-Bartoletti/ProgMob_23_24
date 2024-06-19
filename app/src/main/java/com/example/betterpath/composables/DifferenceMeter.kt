package com.example.betterpath.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedLine(targetValue: Float) {
    val animationProgress = remember { Animatable(0f) }
    val durationMillis = 3000  // Durata dell'animazione in millisecondi

    LaunchedEffect(Unit) {
        animationProgress.animateTo(
            targetValue = targetValue,
            animationSpec = tween(durationMillis)
        )
    }
    Box(
        modifier = Modifier.padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            // Disegna la semi circonferenza
            drawArc(
                //color = Color.Cyan,
                brush = myBrush(size.width / 2, size.height),
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(0f, 0f),
                size = Size(
                    size.width,
                    size.height * 2
                )
            )

            // Calcola la posizione finale lungo la semicirconferenza
            val percentage = animationProgress.value
            val angleInRadians = Math.PI * (1 - percentage)
            val x = size.width / 2 + (size.width / 2) * kotlin.math.cos(angleInRadians).toFloat()
            val y = size.height - (size.height * kotlin.math.sin(angleInRadians).toFloat())

            // Disegna la linea animata
            drawLine(
                color = Color.Black,
                start = Offset(size.width / 2, size.height),
                end = Offset(x, y),
                strokeWidth = 2.dp.toPx()
            )
        }
    }

}
fun myBrush(x: Float, y: Float) : Brush {
    val brush = Brush.sweepGradient(
        colorStops = arrayOf(
            0.0f to Color.Green, //0.60f
            0.45f to Color.Red,//0.50f
            0.70f to Color.Yellow,
            0.8f to Color.Yellow,
            1f to Color.Green
        ),
        center = Offset(x, y)
    )
    return brush
}