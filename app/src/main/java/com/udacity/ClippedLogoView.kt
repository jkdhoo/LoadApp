package com.udacity

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View

class ClippedLogoView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
    }

    private val logoMargin = resources.getDimension(R.dimen.logoMargin)
    private val logoRight = resources.getDimension(R.dimen.logoRight)
    private val logoBottom = resources.getDimension(R.dimen.logoBottom)
    private val logoLeft = resources.getDimension(R.dimen.logoLeft)
    private val logoPadding = resources.getDimension(R.dimen.logoPadding)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLogo(canvas)
    }

    private fun drawLogo(canvas: Canvas) {
        canvas.drawColor(resources.getColor(R.color.colorPrimaryDark, null))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            canvas.clipPath(getArrowPath(), Region.Op.DIFFERENCE)
        } else {
            canvas.clipOutPath(getArrowPath())
        }

        paint.color = resources.getColor(R.color.logo, null)
        canvas.drawRoundRect(
            logoLeft,
            logoMargin + logoPadding * 4,
            logoRight - (logoPadding * 6),
            logoBottom,
            logoRight / 4,
            logoRight / 4,
            paint
        )
        canvas.drawRoundRect(
            logoPadding * 6,
            logoMargin + logoPadding * 6,
            logoRight,
            logoBottom,
            logoRight / 4,
            logoRight / 4,
            paint
        )
        canvas.drawCircle(logoRight / 2, logoBottom / 2, (logoBottom - logoMargin * 2) / 2, paint)
    }

    private fun getArrowPath(): Path {
        val path = Path()
        path.moveTo(10 * logoPadding, logoMargin + 5 * logoPadding)
        path.lineTo(logoRight - 10 * logoPadding, logoMargin + 5 * logoPadding)
        path.lineTo(logoRight - 10 * logoPadding, logoBottom - 9 * logoPadding)
        path.lineTo(logoRight - 10 * logoPadding + logoMargin, logoBottom - 9 * logoPadding)
        path.lineTo(logoRight / 2, logoBottom - logoMargin)
        path.lineTo(10 * logoPadding - logoMargin, logoBottom - 9 * logoPadding)
        path.lineTo(10 * logoPadding, logoBottom - 9 * logoPadding)
        path.lineTo(10 * logoPadding, 5 * logoPadding)
        path.close()
        return path
    }
}