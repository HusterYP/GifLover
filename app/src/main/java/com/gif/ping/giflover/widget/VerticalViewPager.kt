package com.gif.ping.giflover.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @created by PingYuan at 11/4/18
 * @email: husteryp@gmail.com
 * @description:
 */
class VerticalViewPager : ViewPager {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    init {
        setPageTransformer(false, VerticalPagerTransformer())
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(swapXY(ev))
    }

    private fun swapXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = ev.y / height * width
        val newY = ev.x / width * height
        ev.setLocation(newX, newY)
        return ev
    }

    internal inner class VerticalPagerTransformer : ViewPager.PageTransformer {

        override fun transformPage(view: View, v: Float) {
            val pageHeight = view.height
            val pageWidth = view.width

            when {
                v < -1 -> // 看不到的一页
                    view.alpha = 1f
                v < 0 -> { // 滑出的一页
                    view.alpha = 1 + v
                    view.translationY = pageHeight * v
                    view.translationX = pageWidth * -v
                }
                v < 1 -> { // 滑进的一页
                    view.alpha = 1 - v
                    view.translationY = pageHeight * v
                    view.translationX = pageWidth * -v
                }
                else -> // 看不见的另一页
                    view.alpha = 1f
            }
        }
    }
}
