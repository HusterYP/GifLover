package com.example.yuanping.gifbin.bean

import java.io.Serializable

/**
 * @created by PingYuan at 11/3/18
 * @email: husteryp@gmail.com
 * @description:
 */

data class GifBean(var title: String = "", var placeholder: String = "", var gif: String = "") :
    Serializable

data class GifBeans(var gifs: ArrayList<GifBean>, var page: Int, var tag: String) : Serializable