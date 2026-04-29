package com.membership.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

object AppShapes {
    val CardShape = RoundedCornerShape(16.dp)
    val ButtonShape = RoundedCornerShape(12.dp)
    val ChipShape = RoundedCornerShape(20.dp)
    val SearchBarShape = RoundedCornerShape(28.dp)
    val DialogShape = RoundedCornerShape(24.dp)
    val BottomSheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val ImageShape = RoundedCornerShape(12.dp)
    val AvatarShape = RoundedCornerShape(50)
    val TagShape = RoundedCornerShape(8.dp)
}
