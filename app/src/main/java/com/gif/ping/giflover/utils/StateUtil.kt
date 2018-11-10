package com.example.yuanping.gifbin.utils

import android.content.Context
import android.net.ConnectivityManager
import com.gif.ping.giflover.Application
import com.gif.ping.giflover.R

/**
 * @created by PingYuan at 11/4/18
 * @email: husteryp@gmail.com
 * @description: 设备状态检测, 如网络状态等
 */

object StateUtil {
    fun isNetworkConnected(): Boolean {
        val manager = Application.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        val state = networkInfo?.isAvailable ?: false
        if (!state)
            Application.context.toast(R.string.no_network)
        return state
    }
}