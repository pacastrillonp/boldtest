package co.pacastrillon.boldtest.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

object BoldGradients {
    fun hero(): Brush =
        Brush.linearGradient(
            colors = listOf(BoldColors.Navy, BoldColors.ElectricBlue),
            start = Offset(0f, 0f),
            end = Offset(1000f, 1000f)
        )
}