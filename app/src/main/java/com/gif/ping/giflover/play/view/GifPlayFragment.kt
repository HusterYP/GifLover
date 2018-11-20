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
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.yuanping.gifbin.bean.GifBean
import com.gif.ping.giflover.Constant
import com.gif.ping.giflover.R
import com.gif.ping.giflover.widget.PlayerView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

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
        return view
    }

    override fun onDestroyView() {
        releasePlayer()
        super.onDestroyView()
    }

    fun initPlay() {
        val selector = DefaultTrackSelector()
        player = ExoPlayerFactory.newSimpleInstance(context, selector)
        playerView.player = player
        val uri = Uri.parse(gifBean.gif)
        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, Constant.PACKAGE_NAME))
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
}