package com.gif.ping.giflover.utils

import android.app.Activity
import com.gif.ping.giflover.Application
import org.jetbrains.anko.displayMetrics

/**
 * @created by PingYuan at 11/13/18
 * @email: husteryp@gmail.com
 * @description: 设备信息
 */
object ScreenUtil {
    fun dp2px(dpValue: Float): Float {
        return Application.context.resources.displayMetrics.density * dpValue + 0.5f
    }

    fun px2dp(pxValue: Float): Float {
        return pxValue / Application.context.resources.displayMetrics.density + 0.5f
    }

    fun getDeviceWidth() : Int {
        return Application.context.displayMetrics.widthPixels
    }

    fun getDeviceHeight() : Int {
        return Application.context.displayMetrics.heightPixels
    }
}