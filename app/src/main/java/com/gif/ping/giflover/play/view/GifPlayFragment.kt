package com.gif.ping.giflover.play.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.yuanping.gifbin.bean.GifBean
import com.gif.ping.giflover.Application
import com.gif.ping.giflover.Constant
import com.gif.ping.giflover.R
import com.gif.ping.giflover.play.module.VideoCacheManager
import com.gif.ping.giflover.widget.CacheProgressBar
import com.gif.ping.giflover.widget.PlayerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_gif_play.*

/**
 * @created by PingYuan at 11/10/18
 * @email: husteryp@gmail.com
 * @description:
 */
@SuppressLint("ValidFragment")
class GifPlayFragment : Fragment {

    var gifBean: GifBean
    private lateinit var playerView: PlayerView
    private var player: SimpleExoPlayer? = null
    private var cacheProgressBar: CacheProgressBar? = null
    private var cacheRoot: FrameLayout? = null

    companion object {
        fun getInstance(gifBean: GifBean): GifPlayFragment {
            return GifPlayFragment(gifBean)
        }
    }

    constructor(gifBean: GifBean) : super() {
        this.gifBean = gifBean
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gif_play, container, false)
        playerView = view.findViewById(R.id.player_view)
        cacheProgressBar = view.findViewById(R.id.cache_progress)
        cacheRoot = view.findViewById(R.id.cache_root)
        Log.d("@HusterYP","GifPlayFragment onCreateView $id $tag")
        return view
    }

    override fun onDestroyView() {
        releasePlayer()
        Log.d("@HusterYP","GifPlayFragment onDestroyView $id $tag")
        super.onDestroyView()
    }

    fun initPlay() {
        cacheRoot?.visibility = View.GONE
        val selector = DefaultTrackSelector()
        player = ExoPlayerFactory.newSimpleInstance(Application.context, selector)
        playerView.player = player
//        val uri = Uri.parse(gifBean.gif)
        val uri = Uri.parse("${VideoCacheManager.directory}/${gifBean.title}")
        val dataSourceFactory = DefaultDataSourceFactory(Application.context, Util.getUserAgent(Application.context, Constant.PACKAGE_NAME))
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
        player?.prepare(videoSource)
        player?.repeatMode = Player.REPEAT_MODE_ALL
        player?.playWhenReady = true
    }

    fun releasePlayer() {
        player?.release()
        player?.stop(true)
        playerView.player?.release()
        playerView.player = null
    }

    fun updateProgress(progress: Float) {
        cacheProgressBar?.updateProgress(progress)
    }
}