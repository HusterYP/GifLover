package com.gif.ping.giflover.main.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yuanping.gifbin.bean.GifBeans
import com.example.yuanping.gifbin.utils.postDelayed
import com.example.yuanping.gifbin.utils.toast
import com.gif.ping.giflover.R
import com.gif.ping.giflover.main.adapter.MainRecyclerAdapter
import com.gif.ping.giflover.main.module.MainModule
import com.gif.ping.giflover.main.presenter.IMainView
import com.gif.ping.giflover.main.presenter.MainPresenter
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * @created by PingYuan at 11/9/18
 * @email: husteryp@gmail.com
 * @description:
 */
@SuppressLint("ValidFragment")
class MainFragment : Fragment, IMainView {

    private var presenter: MainPresenter? = null
    private var refresh: SwipeRefreshLayout? = null
    private var isInit = false
    private var rootView: View? = null // 缓存rootView
    private lateinit var mainRecycler: RecyclerView
    private lateinit var adapter: MainRecyclerAdapter

    constructor(): super()

    constructor(title: String) : super() {
        presenter = MainPresenter(this, MainModule(title))
    }

    companion object {
        fun getInstance(title: String): MainFragment {
            return MainFragment(title)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false)
            refresh = rootView!!.findViewById(R.id.refresh)
            // TODO 暂时执行假刷新
            refresh?.apply {
                setOnRefreshListener {
                    postDelayed(1000) {
                        context?.toast(R.string.refresh_succeed)
                        isRefreshing = false
                    }
                }
            }
            mainRecycler = rootView!!.findViewById(R.id.fragment_rv_main)
            mainRecycler.addOnScrollListener(LoadMoreListener(presenter))
        }
        return rootView
    }

    fun initView() {
        if (!isInit) {
            presenter?.startRefresh()
            isInit = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (null != rootView) {
            (rootView!!.parent as ViewGroup).removeView(rootView)
        }
    }

    override fun visibleRefresh() {
        refresh?.isRefreshing = true
    }

    override fun invisibleRefresh() {
        refresh?.isRefreshing = false
    }

    // TODO
    override fun visibleLoadMore() {
        Log.d("@HusterYP","Load More")
    }

    // TODO
    override fun invisibleLoadMore() {
        Log.d("@HusterYP","Load Complete")
    }

    override fun noMoreData() {
        context?.toast(R.string.no_more_data)
    }

    override fun setAdapter(gifBeans: GifBeans) {
        fragment_rv_main.layoutManager = LinearLayoutManager(context)
        adapter = MainRecyclerAdapter(context!!, gifBeans)
        fragment_rv_main.adapter = adapter
    }

    override fun updateItems(gifBeans: GifBeans) {
        adapter.gifBeans.gifs.addAll(gifBeans.gifs)
        adapter.gifBeans.page = gifBeans.page
        adapter.notifyDataSetChanged()
    }

    class LoadMoreListener(val presenter: MainPresenter? = null) : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            var lastVisibleItemPosition = 0
            if (recyclerView.layoutManager is LinearLayoutManager) {
                lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == recyclerView.adapter!!.itemCount) {
                presenter?.loadMore()
            }
        }
    }
}