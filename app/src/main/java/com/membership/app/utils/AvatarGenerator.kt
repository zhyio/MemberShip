package com.membership.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import java.io.File
import java.io.FileOutputStream

object AvatarGenerator {

    private data class AvatarTheme(
        val color1: Int,
        val color2: Int,
        val textColor: Int = Color.WHITE
    )

    private val maleThemes = listOf(
        AvatarTheme(Color.parseColor("#4FC3F7"), Color.parseColor("#0288D1")),
        AvatarTheme(Color.parseColor("#26A69A"), Color.parseColor("#00796B")),
        AvatarTheme(Color.parseColor("#5C6BC0"), Color.parseColor("#283593")),
        AvatarTheme(Color.parseColor("#FF7043"), Color.parseColor("#D84315")),
        AvatarTheme(Color.parseColor("#78909C"), Color.parseColor("#37474F"))
    )

    private val femaleThemes = listOf(
        AvatarTheme(Color.parseColor("#F48FB1"), Color.parseColor("#C2185B")),
        AvatarTheme(Color.parseColor("#CE93D8"), Color.parseColor("#7B1FA2")),
        AvatarTheme(Color.parseColor("#FFB74D"), Color.parseColor("#E65100")),
        AvatarTheme(Color.parseColor("#81C784"), Color.parseColor("#2E7D32")),
        AvatarTheme(Color.parseColor("#90CAF9"), Color.parseColor("#1565C0"))
    )

    fun generateAndSave(context: Context, name: String, gender: Int, index: Int): String {
        val size = 512
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val themes = if (gender == 1) maleThemes else femaleThemes
        val theme = themes[index % themes.size]

        // Gradient background
        canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), Paint().apply {
            shader = LinearGradient(
                0f, 0f, size.toFloat(), size.toFloat(),
                theme.color1, theme.color2, Shader.TileMode.CLAMP
            )
            isAntiAlias = true
        })

        // Initials (first character of name)
        val initial = name.first().toString()
        val textPaint = Paint().apply {
            color = theme.textColor
            textSize = size * 0.42f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val textY = size / 2f - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(initial, size / 2f, textY, textPaint)

        // Subtle inner circle decoration
        val circlePaint = Paint().apply {
            color = Color.WHITE
            alpha = 25
            style = Paint.Style.STROKE
            strokeWidth = 3f
            isAntiAlias = true
        }
        canvas.drawCircle(size / 2f, size / 2f, size * 0.38f, circlePaint)

        // Save to internal storage
        val dir = File(context.filesDir, "avatars")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "avatar_${index}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
        }
        bitmap.recycle()

        return file.absolutePath
    }
}
