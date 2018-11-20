package com.gif.ping.giflover.play.view

import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import com.example.yuanping.gifbin.bean.GifBean
import com.example.yuanping.gifbin.utils.immersiveStatusBar
import com.gif.ping.giflover.R
import com.gif.ping.giflover.play.adapter.GifPlayFragmentAdapter
import com.gif.ping.giflover.play.presenter.GifPlayPresenter
import com.gif.ping.giflover.play.presenter.IGifView
import kotlinx.android.synthetic.main.activity_gif_play.*

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
            play_pager.post {
                // 提前加载下一页
                if (position + 2 == play_pager.adapter!!.count) {
                    Log.d("@HusterYP", "loadNext")
                    presenter.loadMorePage(true)
                } else if (position == 1) {
                    presenter.loadMorePage(false)
                    Log.d("@HusterYP", "loadPre")
                }
                val curPage =
                    supportFragmentManager.findFragmentByTag("android:switcher:${play_pager.id}:${play_pager.currentItem}") as GifPlayFragment
//                curPage.initPlay()
                presenter.cacheVideo(curPage.gifBean)
            }
            if (isChanged) {
                val lastPage =
                    supportFragmentManager.findFragmentByTag("android:switcher:${play_pager.id}:$lastIndex") as GifPlayFragment
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

    override fun onDestroy() {
        presenter.cancelDownload()
        super.onDestroy()
    }

    private fun initView() {
        immersiveStatusBar((play_root.background as ColorDrawable).color)
        supportActionBar?.hide()

        tag = intent.getStringExtra(TAG)
        gifBean = intent.getSerializableExtra(CUR_URL) as GifBean
        page = intent.getIntExtra(PAGE, 1)

        play_pager.addOnPageChangeListener(pageChangeListener)
        // TODO bug: 为什么点击第一个Item的时候不会调用onPageSelected, 而点击其他Item会调用onPageSelected
        // 其他Item通过play_pager.currentItem = position就可以调用到onPageSelected了
        play_pager.post { if (play_pager.currentItem == 0) pageChangeListener.onPageSelected(0) }

        presenter = GifPlayPresenter(page, tag, this)
    }

    // 第一次初始化播放列表
    private fun initPlay() {
        presenter.initPlaySet(gifBean)
    }

    override fun setAdapter(gifBeans: ArrayList<GifBean>, position: Int) {
        play_pager.adapter = GifPlayFragmentAdapter(supportFragmentManager, gifBeans)
        play_pager.currentItem = position
    }

    override fun updateAdapter(gifBeans: ArrayList<GifBean>, isNext: Boolean) {
        if (isNext) (play_pager.adapter as GifPlayFragmentAdapter).gifBeans.addAll(gifBeans)
        else {
            val size = gifBeans.size
            gifBeans.addAll((play_pager.adapter as GifPlayFragmentAdapter).gifBeans)
            setAdapter(gifBeans, play_pager.currentItem + size)
        }
        play_pager.adapter?.notifyDataSetChanged()
    }

    override fun showProgress(progress: Int) {

    }
}
