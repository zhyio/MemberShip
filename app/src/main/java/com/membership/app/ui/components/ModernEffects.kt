package com.membership.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.membership.app.ui.theme.AuroraAmber
import com.membership.app.ui.theme.AuroraCyan
import com.membership.app.ui.theme.AuroraRose
import com.membership.app.ui.theme.InkBlack
import com.membership.app.ui.theme.SurfaceGlow

@Composable
fun AuroraBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.background(InkBlack)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF131018),
                        Color(0xFF19151F),
                        Color(0xFF0D1520)
                    )
                )
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AuroraRose.copy(alpha = 0.40f), Color.Transparent)
                ),
                radius = size.minDimension * 0.60f,
                center = Offset(size.width * 0.25f, size.height * 0.08f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AuroraCyan.copy(alpha = 0.28f), Color.Transparent)
                ),
                radius = size.minDimension * 0.64f,
                center = Offset(size.width * 0.80f, size.height * 0.38f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(AuroraAmber.copy(alpha = 0.24f), Color.Transparent)
                ),
                radius = size.minDimension * 0.44f,
                center = Offset(size.width * 0.20f, size.height * 0.78f)
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.18f))
        )
        content()
    }
}

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    containerColor: Color = SurfaceGlow,
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.42f),
                    AuroraCyan.copy(alpha = 0.18f),
                    AuroraRose.copy(alpha = 0.22f)
                )
            )
        ),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(content = content)
    }
}

@Composable
fun GlowOrb(
    modifier: Modifier = Modifier,
    size: Dp = 84.dp,
    color: Color = AuroraRose
) {
    Box(
        modifier = modifier
            .size(size)
            .blur(14.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.38f))
    )
}

@Composable
fun Modifier.hairlineGlassBorder(shape: Shape = RoundedCornerShape(24.dp)): Modifier {
    return border(
        width = 1.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.36f),
                MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
            )
        ),
        shape = shape
    )
}
