package com.gif.ping.giflover.play.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yuanping.gifbin.bean.GifBean
import com.gif.ping.giflover.R
import com.gif.ping.giflover.widget.PlayerView

/**
 * @created by PingYuan at 11/10/18
 * @email: husteryp@gmail.com
 * @description:
 */
@SuppressLint("ValidFragment")
class GifPlayFragment : Fragment {

    private var gifBean: GifBean
    private var playerView: PlayerView

    companion object {
        fun getInstance(gifBean: GifBean): GifPlayFragment {
            return GifPlayFragment(gifBean)
        }
    }

    constructor(gifBean: GifBean) : super() {
        this.gifBean = gifBean
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gif_play, container, false)
        playerView = view.findViewById(R.id.player_view)
        return view
    }

}