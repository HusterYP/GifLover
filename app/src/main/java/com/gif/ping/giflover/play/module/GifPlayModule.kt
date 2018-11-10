package com.gif.ping.giflover.play.module

import com.example.yuanping.gifbin.bean.GifBean
import com.example.yuanping.gifbin.utils.ParseUtil
import com.gif.ping.giflover.Constant
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @created by PingYuan at 11/10/18
 * @email: husteryp@gmail.com
 * @description:
 */
class GifPlayModule {
    private var firstPage = 1
    private var lastPage = 1
    private var tag: String

    interface OnStateChangeListener {
        fun onLoadPlaySetSucceed(gifBeans: ArrayList<GifBean>)
        fun noMorePlaySet()
    }

    constructor(page: Int, tag: String) {
        firstPage = page
        lastPage = page
        this.tag = tag
    }

    fun loadPlaySet(stateChangeListener: OnStateChangeListener) {
        ParseUtil.parseHtml(tag, firstPage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.size <= 0) {
                    stateChangeListener.noMorePlaySet()
                } else {
                    stateChangeListener.onLoadPlaySetSucceed(it)
                }
            }
    }
}