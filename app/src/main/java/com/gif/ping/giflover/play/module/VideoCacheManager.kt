package com.gif.ping.giflover.play.module

import android.util.Log
import com.example.yuanping.gifbin.bean.GifBean
import com.gif.ping.giflover.Application
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * @created by PingYuan at 11/19/18
 * @email: husteryp@gmail.com
 * @description:
 * 视频缓存, 以url为名字, 默认缓存20个
 * 由于服务器不支持断点续传, 所以无法加入断点续传功能
 */

class VideoCacheManager {

    interface OnDownLoadState {
        fun postProgress(progress: Float)
        fun downloadError()
        fun downloadSucceed()
    }

    private lateinit var gifBean: GifBean
    private var oldConnection: HttpURLConnection? = null
    private var buffer = ByteArray(1024)
    private var downloadState: OnDownLoadState
    private val MAX_CACHE_SIZE = 30

    companion object {
        private var instance: VideoCacheManager? = null
        val directory = Application.context.cacheDir.absolutePath

        fun getInstance(downLoadState: OnDownLoadState): VideoCacheManager {
            if (instance == null) {
                synchronized(VideoCacheManager::class.java) {
                    if (instance == null) instance = VideoCacheManager(downLoadState)
                }
            }
            return instance!!
        }
    }

    private constructor(downLoadState: OnDownLoadState) {
        this.downloadState = downLoadState
    }

    fun download(gifBean: GifBean) {
        this.gifBean = gifBean
        cancelLast()
        val path = getCachePath(gifBean)
        val file = File(path)
        if (!file.exists()) file.createNewFile()
        val cacheVideo = FileOutputStream(file)
        var connection: HttpURLConnection? = null
        try {
            val url = URL(gifBean.gif)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.requestMethod = "GET"
            connection.doOutput = false
            connection.doInput = true
            connection.useCaches = false
            connection.connect()
            var length = 0
            oldConnection = connection
            val totalLength = connection.contentLength
            var curLength = 0
            connection.inputStream.use {
                while (true) {
                    length = it!!.read(buffer)
                    if (length <= -1) break
                    cacheVideo.write(buffer, 0, length)
                    curLength += length
                    downloadState.postProgress(curLength.toFloat() / totalLength)
                }
                cacheVideo.flush()
                downloadState.downloadSucceed()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (file.exists()) file.delete()
            Log.d("@HusterYP","Error ${e.message} ; ${gifBean.title}")
        } finally {
            connection?.disconnect()
            cacheVideo.close()
        }
    }

    fun cancelLast() {
        oldConnection?.disconnect()
        oldConnection = null
    }

    fun isVideoExist(gifBean: GifBean): Boolean {
        val file = File("$directory/${gifBean.title}")
        return file.exists()
    }

    private fun getCachePath(gifBean: GifBean): String {
        val file = File(directory)
        if (file.listFiles().size >= MAX_CACHE_SIZE) {
            file.listFiles().sortBy {
                it.lastModified()
            }
            file.listFiles()[0].delete()
        }
        return "$directory/${gifBean.title}"
    }
}

