package com.gif.ping.giflover.play.view

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.example.yuanping.gifbin.bean.GifBean
import com.example.yuanping.gifbin.utils.immersiveStatusBar
import com.example.yuanping.gifbin.utils.toast
import com.gif.ping.giflover.Application
import com.gif.ping.giflover.Constant
import com.gif.ping.giflover.R
import com.gif.ping.giflover.play.adapter.GifPlayRecyclerAdapter
import com.gif.ping.giflover.play.module.VideoCacheManager
import com.gif.ping.giflover.play.presenter.GifPlayPresenter
import com.gif.ping.giflover.play.presenter.IGifView
import com.gif.ping.giflover.widget.CacheProgressBar
import com.gif.ping.giflover.widget.PlayerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_gif_player.*

/**
 * @created by PingYuan at 11/22/18
 * @email: husteryp@gmail.com
 * @description:
 */
class GifPlayerActivity : AppCompatActivity(), IGifView {

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
    private var onScrollListener = object : RecyclerView.OnScrollListener() {

        var firstItemPosition = -1
        var lastItemPosition = -1
        var firstView: View? = null
        var lastView: View? = null
        var cacheRoot: FrameLayout? = null
        var cacheProgress: CacheProgressBar? = null
        var playerView: PlayerView? = null
        var player: SimpleExoPlayer? = null


        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            recyclerView.layoutManager?.apply {
                this as LinearLayoutManager
                val lastPosition = findLastVisibleItemPosition()
                val firstPosition = findFirstVisibleItemPosition()
                if (firstItemPosition < firstPosition) {
                    firstItemPosition = firstPosition
                    lastItemPosition = lastPosition
                    stopPlayer()
                    firstView = recyclerView.getChildAt(0)
                    lastView = recyclerView.getChildAt(childCount - 1)
                } else if (lastItemPosition > lastPosition) {
                    firstItemPosition = firstPosition
                    lastItemPosition = lastPosition
                    stopPlayer()
                    firstView = recyclerView.getChildAt(0)
                    lastView = recyclerView.getChildAt(childCount - 1)
                }

                if (findLastCompletelyVisibleItemPosition() >= 0) getVideo(recyclerView.getChildAt(0))
            }
        }

        fun stopPlayer() {
            player?.release()
            player?.stop(true)
            playerView?.player?.release()
            playerView?.player = null
            cacheProgress = null
            cacheRoot = null
            playerView = null
        }

        fun getVideo(view: View?) {
            view?.apply {
                cacheRoot = view.findViewById(R.id.cache_root)
                cacheProgress = view.findViewById(R.id.cache_progress)
                playerView = view.findViewById(R.id.player_view)
                player_recycler.adapter?.apply {
                    this as GifPlayRecyclerAdapter
                    presenter.cacheVideo(this.gifBeans[firstItemPosition])
                }
            }
        }

        fun startPlay() {
            cacheRoot?.visibility = View.GONE
            val selector = DefaultTrackSelector()
            player = ExoPlayerFactory.newSimpleInstance(Application.context, selector)
            playerView?.player = player
//        val uri = Uri.parse(gifBean.gif)
            player_recycler.adapter?.apply {
                this as GifPlayRecyclerAdapter
                val uri = Uri.parse("${VideoCacheManager.directory}/${this.gifBeans[firstItemPosition].title}")
                val dataSourceFactory = DefaultDataSourceFactory(Application.context, Util.getUserAgent(
                    Application.context, Constant.PACKAGE_NAME))
                val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri)
                player?.prepare(videoSource)
                player?.repeatMode = Player.REPEAT_MODE_ALL
                player?.playWhenReady = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif_player)
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
        onScrollListener.stopPlayer()
        presenter.cancelDownload()
        super.onDestroy()
    }

    private fun initView() {
        immersiveStatusBar((player_refresh.background as ColorDrawable).color)
        supportActionBar?.hide()

        tag = intent.getStringExtra(GifPlayerActivity.TAG)
        gifBean = intent.getSerializableExtra(GifPlayerActivity.CUR_URL) as GifBean
        page = intent.getIntExtra(GifPlayerActivity.PAGE, 1)

        player_recycler.layoutManager = LinearLayoutManager(this)
        player_refresh.setOnRefreshListener {
            presenter.loadMorePage(false)
        }

        presenter = GifPlayPresenter(page, tag, this)
    }

    // 第一次初始化播放列表
    private fun initPlay() {
        presenter.initPlaySet(gifBean)
    }

    override fun setAdapter(gifBeans: ArrayList<GifBean>, position: Int) {
        player_recycler.adapter = GifPlayRecyclerAdapter(gifBeans, this)
        player_recycler.layoutManager?.scrollToPosition(position)
        PagerSnapHelper().attachToRecyclerView(player_recycler)
        player_recycler.addOnScrollListener(onScrollListener)
    }

    override fun updateAdapter(gifBeans: ArrayList<GifBean>, isNext: Boolean) {
        if (isNext) {
            (player_recycler.adapter as GifPlayRecyclerAdapter).addTailData(gifBeans)
        } else {
            val size = gifBeans.size
            (player_recycler.adapter as GifPlayRecyclerAdapter).addPreData(gifBeans)
            player_recycler.layoutManager?.scrollToPosition(size + onScrollListener.firstItemPosition)
        }
        player_refresh.isRefreshing = false
    }

    // TODO bug cacheProgress is null when the last video is not download completely
    // I find the View manually, but still error
    override fun updateProgress(progress: Float) {
        runOnUiThread {
            if (onScrollListener.cacheProgress == null) {
                val view = player_recycler.getChildAt(0)
                onScrollListener.cacheRoot = view.findViewById(R.id.cache_root)
                onScrollListener.cacheProgress = view.findViewById(R.id.cache_progress)
                onScrollListener.playerView = view.findViewById(R.id.player_view)
            }
            onScrollListener.cacheProgress?.updateProgress(progress)
        }
    }

    override fun downloadSucceed() {
        runOnUiThread { onScrollListener.startPlay() }
    }

    override fun noMorePlaySet() {
        player_refresh.isRefreshing = false
        toast(R.string.no_more_play)
    }
}