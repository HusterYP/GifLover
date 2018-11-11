package com.gif.ping.giflover.main.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.yuanping.gifbin.bean.GifBeans
import com.gif.ping.giflover.R
import com.gif.ping.giflover.play.view.GifPlayActivity

/**
 * @created by PingYuan at 11/3/18
 * @email: husteryp@gmail.com
 * @description:
 */

class MainRecyclerAdapter(val context: Context, val gifBeans: GifBeans) :
    RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_rv_main_activity, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gifBeans.gifs.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        Glide.with(context)
            .load(gifBeans.gifs[p1].placeholder)
            .into(p0.item_img)
            .apply {
                RequestOptions().placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
            }
        p0.item_text.text = gifBeans.gifs[p1].title
        (p0.item_img.parent as View).setOnClickListener {
            val intent = Intent(context, GifPlayActivity::class.java)
            intent.putExtra(GifPlayActivity.CUR_URL, gifBeans.gifs[p1])
            intent.putExtra(GifPlayActivity.PAGE, gifBeans.gifs[p1].page)
            intent.putExtra(GifPlayActivity.TAG, gifBeans.tag)
            context.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_img = itemView.findViewById<ImageView>(R.id.item_img)
        var item_text = itemView.findViewById<TextView>(R.id.item_text)
    }
}