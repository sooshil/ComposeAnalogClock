package com.sukajee.analogclock

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sukajee.analogclock.ui.theme.AnalogClockTheme
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun AnalogClock(
    modifier: Modifier = Modifier
) {

    var secondsAngle by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        val instance = Calendar.getInstance()
        val seconds = instance.get(Calendar.SECOND) - 15
        secondsAngle = seconds * 6
        while (true) {
            delay(1000)
            secondsAngle = (secondsAngle + 6) % 360
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Green.copy(alpha = 0.3f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            DigitalClock()
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                drawOuterCircle()
                drawCenter()
                drawInnerDots()
                drawSeconds(secondsAngle)
            }
        }
    }
}

fun DrawScope.drawSeconds(angle: Int) {
    val radius = size.minDimension / 2 - 10.dp.toPx()
    val x = radius * kotlin.math.cos(Math.toRadians(angle.toDouble()).toFloat())
    val y = radius * kotlin.math.sin(Math.toRadians(angle.toDouble()).toFloat())

    drawLine(
        color = Red,
        start = center,
        end = center + Offset(x, y),
        strokeWidth = 2.dp.toPx()
    )
}


fun DrawScope.drawOuterCircle(modifier: Modifier = Modifier) {
    drawCircle(
        color = Blue,
        style = Stroke(width = 6.dp.toPx())
    )
}

fun DrawScope.drawCenter(
    modifier: Modifier = Modifier
) {
    drawCircle(
        color = Blue,
        radius = 6.dp.toPx(),
        center = center,
    )
}

fun DrawScope.drawInnerDots(
    modifier: Modifier = Modifier
) {
    val radius = size.minDimension / 2 - 10.dp.toPx()
    for (i in 0..360 step(6)) {
        val centerOfSmallCircle = center + Offset(
            x = radius * kotlin.math.cos(Math.toRadians(i.toDouble()).toFloat()),
            y = radius * kotlin.math.sin(Math.toRadians(i.toDouble()).toFloat())
        )
        drawCircle(
            color = Red,
            radius = if(i % 30 == 0) 6.dp.toPx() else 4.dp.toPx(),
            center = centerOfSmallCircle
        )
    }
}

@Composable
fun DigitalClock(modifier: Modifier = Modifier) {
    var hour by remember { mutableIntStateOf(0) }
    var minute by remember { mutableIntStateOf(0) }
    var second by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {

        while (true) {
            val instance = Calendar.getInstance()
            hour = instance.get(Calendar.HOUR_OF_DAY)
            minute = instance.get(Calendar.MINUTE)
            second = instance.get(Calendar.SECOND)
            delay(1000)
        }
    }

    Text(
        text = String.format("%02d:%02d:%02d", hour, minute, second),
        modifier = modifier,
        fontSize = 36.sp
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnalogClockTheme {
        AnalogClock()
    }
}