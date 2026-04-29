package com.membership.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import com.membership.app.domain.model.Member

enum class PosterTemplate {
    ROSE_GRADIENT,
    MINIMAL,
    DREAM_PURPLE
}

object PosterGenerator {

    // Layout constants
    private const val W = 1080
    private const val H = 1520
    private const val AVATAR_RADIUS = 190f          // ~35% diameter
    private const val AVATAR_CY = 210f
    private const val CARD_TOP = 340f               // overlaps avatar bottom
    private const val CARD_LEFT = 56f
    private const val CARD_RIGHT = W - 56f
    private const val CARD_BOTTOM = H - 60f
    private const val CARD_RADIUS = 28f
    private const val CARD_PADDING = 48f

    fun generatePoster(
        member: Member,
        context: Context,
        template: PosterTemplate = PosterTemplate.ROSE_GRADIENT
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        when (template) {
            PosterTemplate.ROSE_GRADIENT -> drawRoseGradient(canvas, member, context)
            PosterTemplate.MINIMAL -> drawMinimal(canvas, member, context)
            PosterTemplate.DREAM_PURPLE -> drawDreamPurple(canvas, member, context)
        }

        return bitmap
    }

    // ── Template: Rose Gradient ──
    private fun drawRoseGradient(canvas: Canvas, member: Member, ctx: Context) {
        // Background gradient
        canvas.drawRect(0f, 0f, W.toFloat(), H.toFloat(), Paint().apply {
            shader = LinearGradient(
                0f, 0f, W.toFloat(), H.toFloat(),
                intArrayOf(Color.parseColor("#FF6B6B"), Color.parseColor("#EE5A24"), Color.parseColor("#F0932B")),
                floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.CLAMP
            )
        })
        // Decorative circles
        drawDecorativeCircles(canvas, Color.WHITE, 0.08f)
        // Info card
        drawInfoCard(canvas, Color.WHITE, 245)
        // Avatar with shadow + overlap
        drawAvatar(canvas, member, ctx, W / 2f, AVATAR_CY, AVATAR_RADIUS, shadowColor = Color.parseColor("#40000000"))
        // Content
        drawAllContent(canvas, member, ctx, labelColor = "#999999", valueColor = "#2D2D2D", titleColor = "#FF6B6B", subtitle = "认真找结婚对象")
        // Footer
        drawFooter(canvas, "遇见爱情 · 幸福同行", Color.parseColor("#FF6B6B"))
    }

    // ── Template: Minimal ──
    private fun drawMinimal(canvas: Canvas, member: Member, ctx: Context) {
        canvas.drawRect(0f, 0f, W.toFloat(), H.toFloat(), Paint().apply {
            shader = LinearGradient(
                0f, 0f, W.toFloat(), H.toFloat(),
                intArrayOf(Color.parseColor("#2D3436"), Color.parseColor("#636E72"), Color.parseColor("#B2BEC3")),
                floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.CLAMP
            )
        })
        drawGeometricLines(canvas, Color.WHITE, 0.12f)
        drawInfoCard(canvas, Color.WHITE, 245)
        drawAvatar(canvas, member, ctx, W / 2f, AVATAR_CY, AVATAR_RADIUS, shadowColor = Color.parseColor("#60000000"))
        drawAllContent(canvas, member, ctx, labelColor = "#999999", valueColor = "#2D2D2D", titleColor = "#2D3436", subtitle = "真诚相待 · 期待相遇")
        drawFooter(canvas, "简约生活 · 真诚相待", Color.parseColor("#FFFFFF"))
    }

    // ── Template: Dream Purple ──
    private fun drawDreamPurple(canvas: Canvas, member: Member, ctx: Context) {
        canvas.drawRect(0f, 0f, W.toFloat(), H.toFloat(), Paint().apply {
            shader = LinearGradient(
                0f, 0f, W.toFloat(), H.toFloat(),
                intArrayOf(Color.parseColor("#6C5CE7"), Color.parseColor("#A29BFE"), Color.parseColor("#FD79A8")),
                floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.CLAMP
            )
        })
        drawStars(canvas)
        drawInfoCard(canvas, Color.WHITE, 245)
        drawAvatar(canvas, member, ctx, W / 2f, AVATAR_CY, AVATAR_RADIUS, shadowColor = Color.parseColor("#506C5CE7"))
        drawAllContent(canvas, member, ctx, labelColor = "#999999", valueColor = "#2D2D2D", titleColor = "#6C5CE7", subtitle = "浪漫相遇 · 甜蜜未来")
        drawFooter(canvas, "浪漫相遇 · 甜蜜未来", Color.parseColor("#6C5CE7"))
    }

    // ═══════════════════════════════════════════════════
    //  SHARED DRAWING HELPERS
    // ═══════════════════════════════════════════════════

    // ── Info card (white, rounded, semi-transparent) ──
    private fun drawInfoCard(canvas: Canvas, color: Int, alpha: Int) {
        val rect = RectF(CARD_LEFT, CARD_TOP, CARD_RIGHT, CARD_BOTTOM)
        canvas.drawRoundRect(rect, CARD_RADIUS, CARD_RADIUS, Paint().apply {
            this.color = color
            this.alpha = alpha
            isAntiAlias = true
        })
    }

    // ── Avatar: large circle, shadow, white border, semi-embedded ──
    private fun drawAvatar(canvas: Canvas, member: Member, ctx: Context, cx: Float, cy: Float, radius: Float, shadowColor: Int) {
        // Shadow
        val shadowPaint = Paint().apply {
            color = shadowColor
            isAntiAlias = true
            maskFilter = BlurMaskFilter(24f, BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawCircle(cx, cy + 8f, radius + 4f, shadowPaint)

        // Background ring
        canvas.drawCircle(cx, cy, radius + 6f, Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
        })

        // Photo
        decodeMemberPhoto(member, ctx)?.let { bmp ->
            val save = canvas.saveLayer(cx - radius, cy - radius, cx + radius, cy + radius, null)
            val clip = Path().apply { addCircle(cx, cy, radius, Path.Direction.CW) }
            canvas.clipPath(clip)
            val s = maxOf(radius * 2 / bmp.width, radius * 2 / bmp.height)
            val sw = bmp.width * s
            val sh = bmp.height * s
            canvas.drawBitmap(bmp, null, RectF(cx - sw / 2, cy - sh / 2, cx + sw / 2, cy + sh / 2), null)
            canvas.restoreToCount(save)
        }

        // Thin border
        canvas.drawCircle(cx, cy, radius, Paint().apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 4f
            isAntiAlias = true
        })
    }

    // ── All card content: name, subtitle, info grid, requirements ──
    private fun drawAllContent(canvas: Canvas, member: Member, ctx: Context, labelColor: String, valueColor: String, titleColor: String, subtitle: String) {
        val cx = W / 2f
        val leftMargin = CARD_LEFT + CARD_PADDING
        val contentWidth = (CARD_RIGHT - CARD_LEFT - CARD_PADDING * 2).toInt()

        // ── Name ──
        val nameY = AVATAR_CY + AVATAR_RADIUS + 52f
        Paint().apply {
            color = Color.parseColor(valueColor)
            textSize = 56f
            isFakeBoldText = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }.let { canvas.drawText(member.name, cx, nameY, it) }

        // ── Subtitle ──
        val subY = nameY + 42f
        Paint().apply {
            color = Color.parseColor(labelColor)
            textSize = 26f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }.let { canvas.drawText(subtitle, cx, subY, it) }

        // ── Divider line ──
        val dividerY = subY + 36f
        val divLeft = leftMargin + 60f
        val divRight = CARD_RIGHT - CARD_PADDING - 60f
        canvas.drawLine(divLeft, dividerY, divRight, dividerY, Paint().apply {
            color = Color.parseColor("#E8E8E8")
            strokeWidth = 1.5f
            isAntiAlias = true
        })

        // ── Two-column info grid ──
        val infoStartY = dividerY + 44f
        val labelPaint = Paint().apply {
            color = Color.parseColor(labelColor)
            textSize = 24f
            isAntiAlias = true
        }
        val valuePaint = Paint().apply {
            color = Color.parseColor(valueColor)
            textSize = 28f
            isAntiAlias = true
            typeface = Typeface.DEFAULT_BOLD
        }

        val col1X = leftMargin
        val col2X = leftMargin + contentWidth / 2f + 20f
        val labelOffsetY = 0f
        val valueOffsetY = 30f
        val rowSpacing = 72f

        data class InfoPair(val label: String, val value: String?)

        val pairs = listOfNotNull(
            InfoPair("性别", member.genderText),
            member.age?.let { InfoPair("年龄", "${it}岁") },
            member.height?.let { InfoPair("身高", "${it}cm") },
            member.weight?.let { InfoPair("体重", "${it}kg") },
            InfoPair("学历", member.education),
            InfoPair("职业", member.occupation),
            InfoPair("收入", member.incomeRange),
            InfoPair("现居", member.city),
            InfoPair("籍贯", member.hometown),
            InfoPair("婚姻", member.maritalStatusText),
            if (member.hasChildren) InfoPair("子女", "有" + (member.childrenCount?.let { " (${it}个)" } ?: "")) else null
        )

        var row = 0
        pairs.forEachIndexed { i, (label, value) ->
            if (value.isNullOrBlank()) return@forEachIndexed
            val col = i % 2
            val x = if (col == 0) col1X else col2X
            val y = infoStartY + row * rowSpacing
            canvas.drawText(label, x, y + labelOffsetY, labelPaint)
            canvas.drawText(value, x, y + valueOffsetY, valuePaint)
            if (col == 1) row++
        }
        // If odd count, still advance row
        if (pairs.count { !it.value.isNullOrBlank() } % 2 != 0) row++

        // ── Requirements section ──
        member.requirements?.let { req ->
            val reqStartY = infoStartY + row * rowSpacing + 20f

            // Section divider
            canvas.drawLine(divLeft, reqStartY, divRight, reqStartY, Paint().apply {
                color = Color.parseColor("#E8E8E8")
                strokeWidth = 1.5f
                isAntiAlias = true
            })

            val titleY = reqStartY + 40f
            Paint().apply {
                color = Color.parseColor(titleColor)
                textSize = 30f
                isFakeBoldText = true
                isAntiAlias = true
            }.let { canvas.drawText("择偶要求", leftMargin, titleY, it) }

            val reqPairs = listOfNotNull(
                req.ageRange?.let { InfoPair("年龄", it) },
                req.heightRange?.let { InfoPair("身高", it) },
                req.education?.let { InfoPair("学历", it) },
                req.city?.let { InfoPair("城市", it) },
                req.otherRequirements?.takeIf { o -> o.isNotBlank() }?.let { InfoPair("其他", it) }
            )

            var reqRow = 0
            val reqInfoY = titleY + 44f
            reqPairs.forEachIndexed { i, (label, value) ->
                if (value.isNullOrBlank()) return@forEachIndexed
                val col = i % 2
                val x = if (col == 0) col1X else col2X
                val y = reqInfoY + reqRow * rowSpacing
                canvas.drawText(label, x, y + labelOffsetY, labelPaint)
                canvas.drawText(value, x, y + valueOffsetY, valuePaint)
                if (col == 1) reqRow++
            }
        }
    }

    // ── Footer ──
    private fun drawFooter(canvas: Canvas, text: String, color: Int) {
        Paint().apply {
            this.color = color
            textSize = 26f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        }.let { canvas.drawText(text, W / 2f, CARD_BOTTOM - 28f, it) }
    }

    // ═══════════════════════════════════════════════════
    //  DECORATIVE HELPERS
    // ═══════════════════════════════════════════════════

    private fun drawDecorativeCircles(canvas: Canvas, color: Int, alpha: Float) {
        val p = Paint().apply { this.color = color; this.alpha = (alpha * 255).toInt(); isAntiAlias = true }
        canvas.drawCircle(W * 0.82f, H * 0.08f, 220f, p)
        canvas.drawCircle(W * 0.15f, H * 0.92f, 160f, p)
        canvas.drawCircle(W * 0.92f, H * 0.55f, 110f, p)
    }

    private fun drawGeometricLines(canvas: Canvas, color: Int, alpha: Float) {
        val p = Paint().apply { this.color = color; this.alpha = (alpha * 255).toInt(); strokeWidth = 2f; isAntiAlias = true }
        canvas.drawLine(0f, 0f, W * 0.3f, H * 0.3f, p)
        canvas.drawLine(W.toFloat(), 0f, W * 0.7f, H * 0.3f, p)
        canvas.drawLine(0f, H.toFloat(), W * 0.3f, H * 0.7f, p)
        canvas.drawLine(W.toFloat(), H.toFloat(), W * 0.7f, H * 0.7f, p)
    }

    private fun drawStars(canvas: Canvas) {
        val p = Paint().apply { color = Color.WHITE; alpha = 60; isAntiAlias = true }
        listOf(
            floatArrayOf(W * 0.1f, H * 0.08f),
            floatArrayOf(W * 0.88f, H * 0.12f),
            floatArrayOf(W * 0.12f, H * 0.65f),
            floatArrayOf(W * 0.92f, H * 0.78f),
            floatArrayOf(W * 0.5f, H * 0.04f)
        ).forEach { (x, y) -> drawStar(canvas, x, y, 16f, p) }
    }

    private fun drawStar(canvas: Canvas, cx: Float, cy: Float, size: Float, paint: Paint) {
        val path = Path()
        for (i in 0 until 5) {
            val a = Math.toRadians((i * 72 - 90).toDouble())
            val x = cx + (size * Math.cos(a)).toFloat()
            val y = cy + (size * Math.sin(a)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            val ia = Math.toRadians((i * 72 + 36 - 90).toDouble())
            path.lineTo(cx + (size * 0.4f * Math.cos(ia)).toFloat(), cy + (size * 0.4f * Math.sin(ia)).toFloat())
        }
        path.close()
        canvas.drawPath(path, paint)
    }

    // ── Photo decoder ──
    private fun decodeMemberPhoto(member: Member, context: Context): Bitmap? {
        val source = member.photos.firstOrNull().orEmpty()
        if (source.isBlank()) return null
        return runCatching {
            val uri = Uri.parse(source)
            when (uri.scheme) {
                "content", "android.resource" -> context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
                "file" -> BitmapFactory.decodeFile(uri.path)
                else -> BitmapFactory.decodeFile(source)
            }
        }.getOrNull()
    }
}
