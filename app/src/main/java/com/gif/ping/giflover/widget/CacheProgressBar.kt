package com.gif.ping.giflover.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.gif.ping.giflover.utils.ScreenUtil
import kotlin.math.min

/**
 * @created by PingYuan at 11/21/18
 * @email: husteryp@gmail.com
 * @description:
 */

class CacheProgressBar : View {

    private var progress = 0f
    private var innerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textBounds = Rect()
    private var arcRect = RectF()

    private val INNER_COLOR = Color.parseColor("#000000")
    private val OUTLINE_COLOR = Color.parseColor("#ffffff")
    private val OUTLINE_WIDTH = 10f
    private val TEXT_SIZE = ScreenUtil.dp2px(18f)
    private val TEXT_COLOR = Color.parseColor("#ffffff")

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        innerPaint.color = INNER_COLOR
        innerPaint.style = Paint.Style.FILL

        outlinePaint.color = OUTLINE_COLOR
        outlinePaint.style = Paint.Style.STROKE
        outlinePaint.strokeWidth = OUTLINE_WIDTH
        outlinePaint.strokeCap = Paint.Cap.ROUND

        textPaint.color = TEXT_COLOR
        textPaint.textSize = TEXT_SIZE
    }

    override fun onDraw(canvas: Canvas?) {
        val radius = min(measuredWidth, measuredHeight) / 2f - OUTLINE_WIDTH
        arcRect.left = measuredWidth / 2f - radius
        arcRect.right = measuredWidth / 2f + radius
        arcRect.top = measuredHeight / 2f - radius
        arcRect.bottom = measuredHeight / 2f + radius
        val sweepAngle = progress * 360
        canvas?.apply {
            drawCircle(measuredWidth / 2f, measuredHeight / 2f, radius, innerPaint)
            drawArc(arcRect, -90f, sweepAngle, false, outlinePaint)
            val progressStr = "${(progress * 100).toInt()} %"
            textPaint.getTextBounds(progressStr, 0, progressStr.length, textBounds)
            drawText(progressStr, (measuredWidth - textBounds.width()) / 2f, (measuredHeight + textBounds.height()) / 2f, textPaint)
        }
    }

    fun updateProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }
}