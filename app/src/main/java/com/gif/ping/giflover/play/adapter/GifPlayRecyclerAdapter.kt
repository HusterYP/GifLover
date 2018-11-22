package com.gif.ping.giflover.play.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.yuanping.gifbin.bean.GifBean
import com.gif.ping.giflover.R
import com.gif.ping.giflover.widget.CacheProgressBar
import com.gif.ping.giflover.widget.PlayerView

/**
 * @created by PingYuan at 11/22/18
 * @email: husteryp@gmail.com
 * @description:
 */
class GifPlayRecyclerAdapter : RecyclerView.Adapter<GifPlayRecyclerAdapter.ViewHolder> {

    var gifBeans: ArrayList<GifBean>
    val context: Context

    constructor(gifBeans: ArrayList<GifBean>, context: Context) : super() {
        this.gifBeans = gifBeans
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gif_play, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gifBeans.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(gifBeans[position].placeholder)
            .into(holder.placeHolder)
            .apply {
                // TODO replace the place holder
                RequestOptions().placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
            }
    }

    fun addPreData(gifBeans: ArrayList<GifBean>) {
        gifBeans.addAll(this.gifBeans)
        this.gifBeans = gifBeans
        notifyDataSetChanged()
    }

    fun addTailData(gifBeans: ArrayList<GifBean>) {
        this.gifBeans.addAll(gifBeans)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var playerView = itemView.findViewById<PlayerView>(R.id.player_view)
        var cacheRoot = itemView.findViewById<FrameLayout>(R.id.cache_root)
        var cacheProgress = itemView.findViewById<CacheProgressBar>(R.id.cache_progress)
        var placeHolder = itemView.findViewById<ImageView>(R.id.player_place_holder)
    }
}