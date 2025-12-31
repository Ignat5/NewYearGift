package org.example.project

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import kotlin.random.nextInt

@Composable
fun AdvancedSnowFall(
    modifier: Modifier = Modifier,
    snowflakeCount: Int = 50,
    speed: Float = 1f,
    backgroundColor: Color = Color.Transparent
) {
    val snowflakes = remember { mutableStateListOf<AdvancedSnowflake>() }
    val density = LocalDensity.current

    // Initialize snowflakes
    LaunchedEffect(Unit) {
        repeat(snowflakeCount) {
            snowflakes.add(
                AdvancedSnowflake(
                    x = Random.nextFloat(),
                    y = Random.nextFloat(),
                    size = Random.nextFloat() * 12f + 2f,
                    speed = Random.nextFloat() * 0.4f + 0.3f,
                    rotation = Random.nextFloat() * 360f,
                    rotationSpeed = (Random.nextFloat() - 0.5f) * 2f,
                    type = 2 //Random.nextInt(3) // 0: circle, 1: star, 2: crystal
                )
            )
        }
    }

    // Animation loop
    LaunchedEffect(Unit) {
        while (true) {
            delay(16) // ~60 FPS
            snowflakes.forEachIndexed { index, snowflake ->
                val newY = snowflake.y + 0.004f * snowflake.speed * speed
                val newX = snowflake.x + sin(snowflake.y * 10f) * 0.001f // wave motion
                val newRotation = snowflake.rotation + snowflake.rotationSpeed

                snowflakes[index] = snowflake.copy(
                    x = if (newX > 1f) 0f else if (newX < 0f) 1f else newX,
                    y = if (newY > 1f) 0f else newY,
                    rotation = newRotation % 360f
                )
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        snowflakes.forEach { snowflake ->
            when (snowflake.type) {
                0 -> drawCircleSnowflake(snowflake)
                1 -> drawStarSnowflake(snowflake)
                2 -> drawCrystalSnowflake(snowflake)
            }
        }
    }
}

private data class AdvancedSnowflake(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val type: Int
)

private fun DrawScope.drawCircleSnowflake(snowflake: AdvancedSnowflake) {
    val alpha = 0.6f + snowflake.size * 0.05f
    drawCircle(
        color = Color.White.copy(alpha = alpha),
        radius = snowflake.size,
        center = Offset(
            snowflake.x * size.width,
            snowflake.y * size.height
        )
    )
}

private fun DrawScope.drawStarSnowflake(snowflake: AdvancedSnowflake) {
    val center = Offset(
        snowflake.x * size.width,
        snowflake.y * size.height
    )
    val alpha = 0.7f + snowflake.size * 0.05f

    rotate(snowflake.rotation, center) {
        val outerRadius = snowflake.size
        val innerRadius = snowflake.size * 0.5f
        val points = 5

        val path = Path().apply {
            for (i in 0 until points * 2) {
                val radius = if (i % 2 == 0) outerRadius else innerRadius
                val angle = 3.14f * i / points
                val x = center.x + radius * cos(angle).toFloat()
                val y = center.y + radius * sin(angle).toFloat()

                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }

        drawPath(
            path = path,
            color = Color.White.copy(alpha = alpha),
            style = Fill
        )
    }
}

private fun DrawScope.drawCrystalSnowflake(snowflake: AdvancedSnowflake) {
    val center = Offset(
        snowflake.x * size.width,
        snowflake.y * size.height
    )
    val alpha = 0.8f + snowflake.size * 0.05f

    rotate(snowflake.rotation, center) {
        val radius = snowflake.size
        val arms = 6

        for (i in 0 until arms) {
            val angle = 3.14f * 2 * i / arms
            val endX = center.x + radius * cos(angle).toFloat()
            val endY = center.y + radius * sin(angle).toFloat()

            drawLine(
                color = Color.White.copy(alpha = alpha),
                start = center,
                end = Offset(endX, endY),
                strokeWidth = 1f
            )

            // Add branches
            for (j in 1..2) {
                val branchLength = radius * 0.4f
                val branchPoint = Offset(
                    center.x + (radius * 0.6f) * cos(angle).toFloat(),
                    center.y + (radius * 0.6f) * sin(angle).toFloat()
                )

                val branchAngle1 = angle + 3.14f / 6
                val branchAngle2 = angle - 3.14f / 6

                drawLine(
                    color = Color.White.copy(alpha = alpha * 0.8f),
                    start = branchPoint,
                    end = Offset(
                        branchPoint.x + branchLength * cos(branchAngle1).toFloat(),
                        branchPoint.y + branchLength * sin(branchAngle1).toFloat()
                    ),
                    strokeWidth = 0.8f
                )

                drawLine(
                    color = Color.White.copy(alpha = alpha * 0.8f),
                    start = branchPoint,
                    end = Offset(
                        branchPoint.x + branchLength * cos(branchAngle2).toFloat(),
                        branchPoint.y + branchLength * sin(branchAngle2).toFloat()
                    ),
                    strokeWidth = 0.8f
                )
            }
        }
    }
}