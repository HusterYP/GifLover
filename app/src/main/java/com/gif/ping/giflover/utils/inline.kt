package com.example.yuanping.gifbin.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.gif.ping.giflover.R.id.main_tab_root
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.runOnUiThread

/**
 * @created by PingYuan at 11/4/18
 * @email: husteryp@gmail.com
 * @description:
 */

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    runOnUiThread { Toast.makeText(this, message, duration).show() }
}

fun Context.toast(id: Int, duration: Int = Toast.LENGTH_SHORT) {
    runOnUiThread { Toast.makeText(this, getString(id), duration).show() }
}

fun postDelayed(delayMillis: Long, task: () -> Unit) {
    Handler().postDelayed(task, delayMillis)
}

fun Activity.immersiveStatusBar(color: Int) {
    if (Build.VERSION.SDK_INT >= 21) {
        val decorView = window.decorView
//            var option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        var option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            option = option or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        decorView.systemUiVisibility = option
        window.statusBarColor = color
    }
}