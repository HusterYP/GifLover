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

    constructor(page: Int, tag: String, gifView: IGifView) {
        module = GifPlayModule(page, tag)
        this.gifView = gifView
    }

    fun initPlaySet(url: String) {
        module.loadPlaySet(this)
    }

    fun loadMorePlaySet() {

    }

    override fun onLoadPlaySetSucceed(gifBeans: ArrayList<GifBean>) {
    }

    override fun noMorePlaySet() {
    }
}