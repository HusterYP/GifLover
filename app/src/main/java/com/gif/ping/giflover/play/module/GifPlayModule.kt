package com.gif.ping.giflover.play.module

import android.util.Log
import com.example.yuanping.gifbin.bean.GifBean
import com.example.yuanping.gifbin.utils.ParseUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * @created by PingYuan at 11/10/18
 * @email: husteryp@gmail.com
 * @description:
 */
class GifPlayModule : VideoCacheManager.OnDownLoadState {
    private var firstPage = 1
    private var lastPage = 1
    private var tag: String
    private var stateChangeListener: OnStateChangeListener

    interface OnStateChangeListener {
        fun onInitPlaySucceed(gifBeans: ArrayList<GifBean>)
        fun noMorePlaySet()
        fun loadMoreSucceed(gifBeans: ArrayList<GifBean>, isNext: Boolean = true)
        fun downloadSucceed()
        fun downloadError()
        fun onProgressChange(progress: Float)
    }

    constructor(page: Int, tag: String, stateChangeListener: OnStateChangeListener) {
        firstPage = page
        lastPage = page
        this.tag = tag
        this.stateChangeListener = stateChangeListener
    }

    fun initPlaySet() {
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

    fun loadMorePlaySet( isNext: Boolean = true) {
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

    fun downloadVideo(gifBean: GifBean) {
        if (VideoCacheManager.getInstance(this).isVideoExist(gifBean)) {
            stateChangeListener.downloadSucceed()
            VideoCacheManager.getInstance(this).cancelLast()
            return
        }
        Observable.create(ObservableOnSubscribe<Void> {
            val downloadManager = VideoCacheManager.getInstance(this)
            downloadManager.download(gifBean)
        })
            .subscribeOn(Schedulers.io())
            .subscribe {  }
    }

    fun cancel() {
        VideoCacheManager.getInstance(this).cancelLast()
    }

    override fun postProgress(progress: Float) {
        stateChangeListener.onProgressChange(progress)
    }

    override fun downloadError() {
    }

    override fun downloadSucceed() {
        stateChangeListener.downloadSucceed()
    }
}