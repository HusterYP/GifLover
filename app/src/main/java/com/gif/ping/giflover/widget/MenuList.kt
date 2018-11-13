package com.gif.ping.giflover.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AnticipateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import com.gif.ping.giflover.R
import com.gif.ping.giflover.utils.ScreenUtil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * @created by PingYuan at 11/13/18
 * @email: husteryp@gmail.com
 * @description:
 */
class MenuList : FrameLayout {

    var fabMenu: FloatingActionButton = FloatingActionButton(context)
    var fabMine: FloatingActionButton = FloatingActionButton(context)
    var fabSearch: FloatingActionButton = FloatingActionButton(context)
    var fabSetting: FloatingActionButton = FloatingActionButton(context)
    private var isMenuOpen = false
    private val RADIUS = 300
    private val RADIUS_CENTER = 260
    private val MAX_RADIUS = max(ScreenUtil.getDeviceHeight(), ScreenUtil.getDeviceWidth())
    private lateinit var animator: ValueAnimator
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var curUpdatePercent = 0.0f
    private var newLayerUpdatePercent = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initView()
        setWillNotDraw(false)
        fabMenu.setOnClickListener {
            if (isMenuOpen) fabMenu.animate().rotationBy(315f).setInterpolator(BounceInterpolator()).setDuration(2000).start()
            else fabMenu.animate().rotationBy(-315f).setInterpolator(BounceInterpolator()).setDuration(2000).start()
            animator = ValueAnimator.ofFloat(0f, 100f)
            animator.duration = 500
            animator.addUpdateListener {
                curUpdatePercent = it.animatedValue as Float / 100f
                if (isMenuOpen) newLayerUpdatePercent = max(newLayerUpdatePercent - 4, 0)
                else newLayerUpdatePercent = min(newLayerUpdatePercent + 5, 100)
                if (!isMenuOpen) openMenu()
                else closeMenu()
                invalidate()
            }
            if (!isMenuOpen) animator.interpolator = BounceInterpolator()
            else animator.interpolator = AnticipateInterpolator()
            animator.start()
        }

        paint.color = Color.parseColor("#333333")
        paint.alpha = 150
    }

    private fun initView() {
        fabMenu.size = FloatingActionButton.SIZE_NORMAL
        fabMine.size = FloatingActionButton.SIZE_NORMAL
        fabSearch.size = FloatingActionButton.SIZE_NORMAL
        fabSetting.size = FloatingActionButton.SIZE_NORMAL
        fabMine.setImageResource(R.mipmap.menu_mine)
        fabSearch.setImageResource(R.mipmap.menu_search)
        fabSetting.setImageResource(R.mipmap.menu_settinng)
        fabMenu.setImageResource(R.mipmap.menu_add)
        val params = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        params.gravity = Gravity.BOTTOM or Gravity.END
        params.marginEnd = ScreenUtil.dp2px(10f).toInt()
        params.bottomMargin = ScreenUtil.dp2px(20f).toInt()
        addView(fabMine, params)
        addView(fabSearch, params)
        addView(fabSetting, params)
        addView(fabMenu, params)
    }

    override fun onDraw(canvas: Canvas?) {
        if (isMenuOpen) {
            canvas?.drawCircle(fabMenu.x,
                fabMenu.y,
                MAX_RADIUS * (newLayerUpdatePercent / 100f),
                paint
            )
        } else {
            canvas?.drawCircle(fabMenu.x,
                fabMenu.y,
                MAX_RADIUS * (newLayerUpdatePercent / 100f),
                paint
            )
        }
    }

    private fun openMenu() {
        if (curUpdatePercent >= 1.0f) {
            isMenuOpen = true
            newLayerUpdatePercent = 100
        }
        val x = fabMenu.x
        val y = fabMenu.y
        fabSetting.apply {
            layout(
                (x - RADIUS * curUpdatePercent).toInt(),
                y.toInt(),
                (x - RADIUS * curUpdatePercent + width).toInt(),
                (y + height).toInt()
            )
            scaleX = curUpdatePercent
            scaleY = curUpdatePercent
            alpha = curUpdatePercent
        }
        fabMine.apply {
            layout(
                x.toInt(),
                (y - RADIUS * curUpdatePercent).toInt(),
                (x + width).toInt(),
                (y - RADIUS * curUpdatePercent + height).toInt()
            )
            scaleX = curUpdatePercent
            scaleY = curUpdatePercent
            alpha = curUpdatePercent
        }
        fabSearch.apply {
            layout(
                (x - RADIUS_CENTER * curUpdatePercent * sin(45f)).toInt(),
                (y - RADIUS_CENTER * curUpdatePercent * sin(45f)).toInt(),
                (x - RADIUS_CENTER * curUpdatePercent * sin(45f) + width).toInt(),
                (y - RADIUS_CENTER * curUpdatePercent * sin(45f) + height).toInt()
            )
            scaleX = curUpdatePercent
            scaleY = curUpdatePercent
            alpha = curUpdatePercent
        }
    }

    private fun closeMenu() {
        if (curUpdatePercent >= 1.0f) {
            isMenuOpen = false
            newLayerUpdatePercent = 0
        }
        var fabX = fabMenu.x.toInt() - RADIUS
        var fabY = fabMenu.y.toInt() - RADIUS
        fabSetting.apply {
            layout(
                (fabX + RADIUS * curUpdatePercent).toInt(),
                y.toInt(),
                (fabX + RADIUS * curUpdatePercent + width).toInt(),
                (y + height).toInt()
            )
            scaleX = 1 - curUpdatePercent
            scaleY = 1 - curUpdatePercent
            alpha = 1 - curUpdatePercent
        }
        fabMine.apply {
            layout(
                x.toInt(),
                (fabY + RADIUS * curUpdatePercent).toInt(),
                x.toInt() + width,
                (fabY + RADIUS * curUpdatePercent + height).toInt()
            )
            scaleX = 1 - curUpdatePercent
            scaleY = 1 - curUpdatePercent
            alpha = 1 - curUpdatePercent
        }
        fabX = (fabMenu.x.toInt() - RADIUS_CENTER * sin(45f)).toInt()
        fabY = (fabMenu.y.toInt() - RADIUS_CENTER * sin(45f)).toInt()
        fabSearch.apply {
            layout(
                (fabX + RADIUS_CENTER * curUpdatePercent * sin(45f)).toInt(),
                (fabY + RADIUS_CENTER * curUpdatePercent * sin(45f)).toInt(),
                (fabX + RADIUS_CENTER * curUpdatePercent * sin(45f) + width).toInt(),
                (fabY + RADIUS_CENTER * curUpdatePercent * sin(45f) + height).toInt()
            )
            scaleX = 1 - curUpdatePercent
            scaleY = 1 - curUpdatePercent
            alpha = 1 - curUpdatePercent
        }
    }
}