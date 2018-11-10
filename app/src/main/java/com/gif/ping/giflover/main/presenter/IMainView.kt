package com.gif.ping.giflover.main.presenter

import com.example.yuanping.gifbin.bean.GifBeans

/**
 * @created by PingYuan at 11/9/18
 * @email: husteryp@gmail.com
 * @description:
 */
interface IMainView {
    // 开始刷新
    fun visibleRefresh()

    // 结束刷新
    fun invisibleRefresh()

    // 加载更多
    fun visibleLoadMore()

    // 结束加载更多
    fun invisibleLoadMore()

    fun setAdapter(gifBeans: GifBeans)

    fun updateItems(gifBeans: GifBeans)

    fun noMoreData()
}