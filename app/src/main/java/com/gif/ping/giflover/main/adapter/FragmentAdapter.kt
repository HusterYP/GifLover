package com.gif.ping.giflover.main.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.gif.ping.giflover.main.view.MainFragment

/**
 * @created by PingYuan at 11/9/18
 * @email: husteryp@gmail.com
 * @description:
 */
class FragmentAdapter : FragmentPagerAdapter {

    private var titles: List<String>

    constructor(manager: FragmentManager, titles: List<String>) : super(manager) {
        this.titles = titles
    }

    override fun getItem(position: Int): Fragment {
        return MainFragment.getInstance(titles[position])
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles.get(position)
    }
}