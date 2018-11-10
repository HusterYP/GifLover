package com.gif.ping.giflover.play.view

import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.yuanping.gifbin.bean.GifBean
import com.example.yuanping.gifbin.utils.immersiveStatusBar
import com.gif.ping.giflover.R
import kotlinx.android.synthetic.main.activity_gif_play.*

class GifPlayActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TAG"
        const val PAGE = "PAGE"
        const val CUR_URL = "URL"
    }

    private lateinit var tag: String
    private var page = 1
    private lateinit var curUrl: GifBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif_play)
        initView()
    }

    private fun initView() {
        immersiveStatusBar((play_root.background as ColorDrawable).color)
        supportActionBar?.hide()

        tag = intent.getStringExtra(TAG)
        curUrl = intent.getSerializableExtra(CUR_URL) as GifBean
        page = intent.getIntExtra(PAGE, 1)
    }
}
