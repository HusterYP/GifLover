package com.gif.ping.giflover.play.presenter

import com.example.yuanping.gifbin.bean.GifBean

/**
 * @created by PingYuan at 11/10/18
 * @email: husteryp@gmail.com
 * @description:
 */
interface IGifView {
    fun setAdapter(gifBeans: ArrayList<GifBean>, position: Int)
    fun updateAdapter(gifBeans: ArrayList<GifBean>, isNext: Boolean = true)
}