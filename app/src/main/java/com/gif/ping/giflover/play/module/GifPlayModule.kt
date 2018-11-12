package com.gif.ping.giflover.play.module

import com.example.yuanping.gifbin.bean.GifBean
import com.example.yuanping.gifbin.utils.ParseUtil
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
        fun onInitPlaySucceed(gifBeans: ArrayList<GifBean>)
        fun noMorePlaySet()
        fun loadMoreSucceed(gifBeans: ArrayList<GifBean>, isNext: Boolean = true)
    }

    constructor(page: Int, tag: String) {
        firstPage = page
        lastPage = page
        this.tag = tag
    }

    fun initPlaySet(stateChangeListener: OnStateChangeListener) {
        ParseUtil.parseHtml(tag, firstPage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.size <= 0) {
                    stateChangeListener.noMorePlaySet()
                } else {
                    stateChangeListener.onInitPlaySucceed(it)
                }
            }
    }

    fun loadMorePlaySet(stateChangeListener: OnStateChangeListener, isNext: Boolean = true) {
        when {
            isNext -> ParseUtil.parseHtml(tag,
                ++lastPage
            ).observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it.size <= 0) {
                    // TODO 没有更多提示, 需要注意这里是加载更多的时候, 而不是初次加载
                } else {
                    stateChangeListener.loadMoreSucceed(it, true)
                }
            }
            --firstPage > 0 -> ParseUtil.parseHtml(tag,
                firstPage
            ).observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it.size <= 0) {
                    // TODO 没有更多提示, 需要注意这里是加载更多的时候, 而不是初次加载
                } else {
                    stateChangeListener.loadMoreSucceed(it, false)
                }
            }
            else -> {
                // TODO 没有更多提示, 需要注意这里是加载更多的时候, 而不是初次加载
            }
        }
    }
}