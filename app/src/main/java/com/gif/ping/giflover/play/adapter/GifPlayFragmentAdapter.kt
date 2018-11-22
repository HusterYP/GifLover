package com.gif.ping.giflover.play.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.yuanping.gifbin.bean.GifBean
import com.gif.ping.giflover.play.view.GifPlayFragment

/**
 * @created by PingYuan at 11/10/18
 * @email: husteryp@gmail.com
 * @description:
 */
class GifPlayFragmentAdapter : FragmentPagerAdapter {

    var gifBeans: ArrayList<GifBean>

    constructor(manager: FragmentManager, gifBeans: ArrayList<GifBean>) : super(manager) {
        this.gifBeans = gifBeans
    }

    override fun getItem(position: Int): Fragment {
//        return GifPlayFragment.getInstance(gifBeans[position])
        return GifPlayFragment(gifBeans[position])
    }

    override fun getCount(): Int {
        return gifBeans.size
    }

}