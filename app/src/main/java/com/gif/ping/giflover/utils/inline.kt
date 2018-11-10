package com.example.yuanping.gifbin.utils

import android.content.Context
import android.os.Handler
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
