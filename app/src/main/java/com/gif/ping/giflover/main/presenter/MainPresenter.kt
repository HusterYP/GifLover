package com.gif.ping.giflover.main.presenter

import com.example.yuanping.gifbin.bean.GifBeans
import com.gif.ping.giflover.main.module.MainModule

/**
 * @created by PingYuan at 11/9/18
 * @email: husteryp@gmail.com
 * @description:
 */
class MainPresenter(val mainView: IMainView?, val mainModule: MainModule) :
    MainModule.OnStateChangeListener {

    override fun onRefreshSucceed(gifBeans: GifBeans) {
        mainView?.invisibleRefresh()
        mainView?.setAdapter(gifBeans)
    }

    override fun onRefreshError() {
    }

    override fun onLoadMoreSucceed(gifBeans: GifBeans) {
        mainView?.invisibleLoadMore()
        mainView?.updateItems(gifBeans)
    }

    override fun noMoreData() {
        mainView?.apply {
            invisibleLoadMore()
            noMoreData()
        }
    }

    fun startRefresh() {
        mainView?.visibleRefresh()
        mainModule.refresh(this)
    }

    fun loadMore() {
        mainView?.visibleLoadMore()
        mainModule.loadMore(this)
    }
}