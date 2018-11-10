package com.gif.ping.giflover.main.module

import android.util.Log
import com.example.yuanping.gifbin.bean.GifBeans
import com.example.yuanping.gifbin.utils.ParseUtil
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @created by PingYuan at 11/9/18
 * @email: husteryp@gmail.com
 * @description:
 */
class MainModule {

    private var tag: String
    private var page: Int = 1

    constructor(tag: String) {
        this.tag = tag
    }

    interface OnStateChangeListener {
        fun onRefreshSucceed(gifBeans: GifBeans)
        fun onRefreshError()
        fun onLoadMoreSucceed(gifBeans: GifBeans)
        fun noMoreData()
    }

    fun refresh(stateListener: OnStateChangeListener) {
        ParseUtil.parseHtml(tag, 1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val gifBeans = GifBeans(it, page, tag)
                stateListener.onRefreshSucceed(gifBeans)
            }
    }

    fun loadMore(onStateChangeListener: OnStateChangeListener) {
        page++
        ParseUtil.parseHtml(tag, page)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.size <= 0) {
                    onStateChangeListener.noMoreData()
                } else {
                    val gifBeans = GifBeans(it, page, tag)
                    onStateChangeListener.onLoadMoreSucceed(gifBeans)
                }
            }
    }
}