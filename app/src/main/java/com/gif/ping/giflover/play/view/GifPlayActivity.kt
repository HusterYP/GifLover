package com.gif.ping.giflover.play.view

import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import com.example.yuanping.gifbin.bean.GifBean
import com.example.yuanping.gifbin.utils.immersiveStatusBar
import com.gif.ping.giflover.R
import com.gif.ping.giflover.main.view.MainFragment
import com.gif.ping.giflover.play.adapter.GifPlayFragmentAdapter
import com.gif.ping.giflover.play.presenter.GifPlayPresenter
import com.gif.ping.giflover.play.presenter.IGifView
import kotlinx.android.synthetic.main.activity_gif_play.*
import kotlinx.android.synthetic.main.activity_main.*

class GifPlayActivity : AppCompatActivity(), IGifView {

    companion object {
        const val TAG = "TAG"
        const val PAGE = "PAGE"
        const val CUR_URL = "URL"
    }

    private lateinit var tag: String
    private var page = 1
    private lateinit var gifBean: GifBean
    private lateinit var presenter: GifPlayPresenter
    private var isInit = false
    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        private var lastIndex = 0
        private var isChanged = false
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            play_pager.post{
                val curPage = supportFragmentManager.findFragmentByTag("android:switcher:${play_pager.id}:${play_pager.currentItem}") as GifPlayFragment
                curPage.initPlay()
            }
            if (isChanged) {
                val lastPage = supportFragmentManager.findFragmentByTag("android:switcher:${play_pager.id}:$lastIndex") as GifPlayFragment
                lastPage.releasePlayer()
            }
            isChanged = true
            lastIndex = position
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif_play)
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (!isInit) {
            initPlay()
            isInit = true
        }
    }

    private fun initView() {
        immersiveStatusBar((play_root.background as ColorDrawable).color)
        supportActionBar?.hide()

        tag = intent.getStringExtra(TAG)
        gifBean = intent.getSerializableExtra(CUR_URL) as GifBean
        page = intent.getIntExtra(PAGE, 1)

        play_pager.addOnPageChangeListener(pageChangeListener)

        presenter = GifPlayPresenter(page, tag, this)
    }

    // 第一次初始化播放列表
    private fun initPlay() {
        presenter.loadMorePlaySet(gifBean)
    }

    override fun setAdapter(gifBeans: ArrayList<GifBean>, position: Int) {
        play_pager.adapter = GifPlayFragmentAdapter(supportFragmentManager, gifBeans)
        play_pager.currentItem = position
    }
}
