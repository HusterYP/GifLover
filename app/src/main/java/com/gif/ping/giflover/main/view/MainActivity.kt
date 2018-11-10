package com.gif.ping.giflover.main.view

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.yuanping.gifbin.utils.toast
import com.gif.ping.giflover.R
import com.gif.ping.giflover.main.adapter.FragmentAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val titles = ArrayList<String>()
    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            val page = supportFragmentManager.findFragmentByTag("android:switcher:" + main_pager.id + ":" + main_pager.currentItem) as MainFragment
            page.initView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        immersiveStatusBar()
        supportActionBar?.hide()

        titles.add("funny")
        titles.add("cats")
        titles.add("kids")
        titles.add("dogs")
        main_pager.adapter = FragmentAdapter(supportFragmentManager, titles)
        main_tab.setupWithViewPager(main_pager)
        main_pager.addOnPageChangeListener(pageChangeListener)
        main_pager.post { pageChangeListener.onPageSelected(0) }

        main_add.setOnClickListener {
            // TODO
            toast("add items")
        }
    }

    private fun immersiveStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
//            var option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            var option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                option = option or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            decorView.systemUiVisibility = option
            window.statusBarColor = (main_tab_root.background as ColorDrawable).color
        }
    }
}