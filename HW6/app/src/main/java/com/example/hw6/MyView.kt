package com.example.hw6


import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min


class MyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var duration: Int = 1000 * 60 * 60 * 24
    var isInfinite: Boolean = true
    private var ended = false
    private var startTime: Long

    private val rectSize = dp(64f)
    private val rectHypot = hypot(rectSize, rectSize)
    private val rectCornerRadius = dp(8f)
    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = R.color.colorGray.toInt()
    }
    private val rectF = RectF()
    private var rectRotation: Float = 0f
    private val rectDelta = 2.5f

    private val circleRadius = dp(32f)
    private val circleDia = circleRadius * 2
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = R.color.colorGray.toInt()
    }
    private var circleScale = 1f
    private val circleDelta = 0.015f
    private val circleMaxScale = 1.4f
    private var circleUp = true

    private val margin = dp(16f)

    private val width = (rectHypot + circleDia * circleMaxScale + margin)
    private val height = max(rectHypot, circleDia * circleMaxScale)

    init {
        val a: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MyView, defStyleAttr, defStyleRes)
        try {
            duration = a.getInt(R.styleable.MyView_duration, 1000 * 60 * 60 * 24)
            if (duration < 0) {
                duration = 0
                ended = true
            }
            isInfinite = a.getBoolean(R.styleable.MyView_isInfinite, true)
        } finally {
            a.recycle()
        }
        startTime = System.currentTimeMillis()
        setOnClickListener {
            startTime = System.currentTimeMillis()
            if (duration > 0) ended = false
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var save = canvas.save()

        val cx = rectHypot / 2
        val cy = rectHypot / 2

        canvas.rotate(rectRotation, cx, cy)

        val l = cx - rectSize / 2
        val t = cy - rectSize / 2

        rectF.set(l, t, l + rectSize, t + rectSize)
        canvas.drawRoundRect(rectF, rectCornerRadius, rectCornerRadius, rectPaint)

        canvas.restoreToCount(save)
        save = canvas.save()

        canvas.translate(rectHypot + margin, (height / 2 - circleRadius))
        canvas.scale(circleScale, circleScale, circleRadius, circleRadius)
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, circlePaint)

        update()
        //invalidate()
        canvas.restoreToCount(save)
    }

    private fun getSize(measureSpec: Int, desired: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.AT_MOST -> min(size, desired)
            MeasureSpec.EXACTLY -> size
            MeasureSpec.UNSPECIFIED -> desired
            else -> desired
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getSize(widthMeasureSpec, width.toInt()),
            getSize(heightMeasureSpec, height.toInt())
        )
    }

    private fun update() {
        if (System.currentTimeMillis() - startTime >= duration) ended = true
        if (!(isInfinite || !ended)) return
        rectRotation += rectDelta
        if (circleUp) {
            circleScale += circleDelta
            if (circleScale >= circleMaxScale) circleUp = false
        } else {
            circleScale -= circleDelta
            if (circleScale <= 1.0f) circleUp = true
        }
        invalidate()
    }

    private fun dp(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

}
