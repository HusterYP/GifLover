package com.gif.ping.giflover.play.module

import android.util.Log
import com.example.yuanping.gifbin.bean.GifBean
import java.net.HttpURLConnection
import java.net.URL

/**
 * @created by PingYuan at 11/19/18
 * @email: husteryp@gmail.com
 * @description:
 * 视频缓存, 以url为名字, 默认缓存20个
 * 由于服务器不支持断点续传, 所以无法加入断点续传功能
 */

class VideoCacheManager private constructor() {
    private lateinit var gifBean: GifBean
    private var oldConnection: HttpURLConnection? = null
    private var buffer = ByteArray(1024)

    companion object {
        private var instance: VideoCacheManager? = null

        fun getInstance(): VideoCacheManager {
            if (instance == null) {
                synchronized(VideoCacheManager::class.java) {
                    if (instance == null) instance = VideoCacheManager()
                }
            }
            return instance!!
        }
    }

    fun download(gifBean: GifBean) {
        this.gifBean = gifBean
        cancelLast()
        var connection: HttpURLConnection? = null
        try {
            val url = URL(gifBean.gif)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.requestMethod = "GET"
            connection.connect()
            var length = 0
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                oldConnection = connection
                connection.inputStream.use {
                    while (true) {
                        length = it!!.read(buffer)
                        if (length == -1) break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("@HusterYP","Error ${e.message} $connection  ${gifBean.title}")
        } finally {
            connection?.disconnect()
        }
    }

    fun cancelLast() {
        oldConnection?.disconnect()
        oldConnection = null
    }
}

