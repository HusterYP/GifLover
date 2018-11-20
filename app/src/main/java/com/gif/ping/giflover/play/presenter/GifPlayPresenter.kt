package com.gif.ping.giflover.play.presenter

import android.util.Log
import com.example.yuanping.gifbin.bean.GifBean
import com.gif.ping.giflover.play.module.GifPlayModule

/**
 * @created by PingYuan at 11/10/18
 * @email: husteryp@gmail.com
 * @description:
 */
class GifPlayPresenter : GifPlayModule.OnStateChangeListener {
    private var module: GifPlayModule
    private var gifView: IGifView
    private var curGif: GifBean? = null

    constructor(page: Int, tag: String, gifView: IGifView) {
        module = GifPlayModule(page, tag)
        this.gifView = gifView
    }

    fun initPlaySet(curGif: GifBean? = null) {
        module.initPlaySet(this)
        this.curGif = curGif
    }

    fun loadMorePage(isNext: Boolean = true) {
        if (isNext) module.loadMorePlaySet(this, true)
        else module.loadMorePlaySet(this, false)
    }

    fun cacheVideo(gifBean: GifBean) {
        gifView.showProgress(0)
        module.downloadVideo(gifBean)
    }

    fun cancelDownload() {
        module.cancel()
    }

    override fun onInitPlaySucceed(gifBeans: ArrayList<GifBean>) {
        curGif?.apply {
            val index = gifBeans.indexOfFirst {
                it.gif == this.gif
            }
            if (index < 0) {
                // TODO 添加错误回调
            } else {
                gifView.setAdapter(gifBeans, index)
            }
        }
    }

    override fun noMorePlaySet() {
    }

    override fun loadMoreSucceed(gifBeans: ArrayList<GifBean>, isNext: Boolean) {
        gifView.updateAdapter(gifBeans, isNext)
    }

    override fun downloadError() {
    }

    override fun downloadSucceed() {
    }
}