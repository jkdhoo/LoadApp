package com.udacity.custom_view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.udacity.ButtonState
import com.udacity.R
import timber.log.Timber
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    var loadingButton: LoadingButton

    private var widthSize = 0
    private var heightSize = 0
    private var progress = 0F
    private var angle = 0F
    private var buttonText = ""
    private var buttonBackground = 0

    private val paint = Paint().apply {
        isAntiAlias = true
        textSize = 64F
        textAlign = Paint.Align.CENTER
    }

    private var buttonAnimator = ValueAnimator()
    private var circleAnimator = ValueAnimator()

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Completed -> {
                stopAnimations()
            }
            ButtonState.Clicked -> Timber.i("Placeholder")
            ButtonState.Loading -> {
                animatedCircle()
                animatedButton()
            }
        }
        Timber.i("$new")
    }

    init {
        isClickable = true
        loadingButton = findViewById(R.id.custom_button)
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonBackground = getColor(R.styleable.LoadingButton_buttonBackground, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawButton(canvas)
        if (buttonState == ButtonState.Loading) {
            drawButtonFill(canvas)
            drawCircle(canvas)
        }
        drawText(canvas, buttonState)
    }

    private fun drawButton(canvas: Canvas?) {
        paint.color = buttonBackground
        canvas?.drawRect(0F, 0F, widthSize.toFloat(), heightSize.toFloat(), paint)
    }

    private fun drawText(canvas: Canvas?, buttonState: ButtonState) {
        paint.color = ResourcesCompat.getColor(resources, R.color.white, null)
        buttonText = when (buttonState) {
            ButtonState.Loading -> context.getString(R.string.button_text_loading)
            else -> context.getString(R.string.button_text)
        }
        canvas?.drawText(buttonText, widthSize / 2F, (heightSize / 2F) - ((paint.descent() + paint.ascent()) / 2F), paint)
    }

    private fun animatedButton() {
        buttonAnimator = ValueAnimator.ofFloat(0F, widthSize.toFloat()).apply {
            duration = 1000
            addUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Float
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                valueAnimator.repeatMode = ValueAnimator.REVERSE
                valueAnimator.interpolator = LinearInterpolator()
                loadingButton.invalidate()
            }
            disableViewDuringAnimation(loadingButton)
            start()
        }
    }

    private fun drawButtonFill(canvas: Canvas?) {
        paint.color = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
        canvas?.drawRect(
            0f,
            0f,
            progress,
            heightSize.toFloat(), paint)
    }

    private fun animatedCircle() {
        circleAnimator = ValueAnimator.ofFloat(0F, 360F).apply {
            duration = 1000
            addUpdateListener { valueAnimator ->
                angle = valueAnimator.animatedValue as Float
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                valueAnimator.repeatMode = ValueAnimator.REVERSE
                loadingButton.invalidate()
            }
            disableViewDuringAnimation(loadingButton)
            start()
        }
    }

    private fun drawCircle(canvas: Canvas?) {
        paint.color= Color.YELLOW
        canvas?.drawArc((widthSize.toFloat() - 150f),(heightSize.toFloat() / 2) - 50f, (widthSize.toFloat()-50f),
            (heightSize.toFloat() / 2) + 50f, 0F,angle, true,paint)
    }

    private fun stopAnimations() {
        buttonAnimator.end()
        progress = 0F
        circleAnimator.end()
        angle = 0F
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        invalidate()
        return true
    }

    private fun ValueAnimator.disableViewDuringAnimation(view: View) {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }
}