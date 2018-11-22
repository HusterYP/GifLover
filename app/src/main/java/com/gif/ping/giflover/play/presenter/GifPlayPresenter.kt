package com.gif.ping.giflover.play.presenter

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
        module = GifPlayModule(page, tag, this)
        this.gifView = gifView
    }

    fun initPlaySet(curGif: GifBean? = null) {
        module.initPlaySet()
        this.curGif = curGif
    }

    fun loadMorePage(isNext: Boolean = true) {
        if (isNext) module.loadMorePlaySet(true)
        else module.loadMorePlaySet(false)
    }

    fun cacheVideo(gifBean: GifBean) {
        gifView.updateProgress(0f)
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
        gifView.downloadSucceed()
    }

    override fun onProgressChange(progress: Float) {
        gifView.updateProgress(progress)
    }
}