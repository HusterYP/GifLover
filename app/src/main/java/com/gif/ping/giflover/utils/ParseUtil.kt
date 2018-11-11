package com.example.yuanping.gifbin.utils

import com.example.yuanping.gifbin.bean.GifBean
import com.gif.ping.giflover.Application
import com.gif.ping.giflover.Constant
import com.gif.ping.giflover.db.GifBaseHolder
import com.gif.ping.giflover.db.GifEntity
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup

/**
 * @created by PingYuan at 11/4/18
 * @email: husteryp@gmail.com
 * @description:
 */

object ParseUtil {
    private val gson = Gson()

    fun parseHtml(url: String): Observable<ArrayList<GifBean>> {
        return Observable.create(ObservableOnSubscribe<ArrayList<GifBean>> {
            val document = Jsoup.connect(url)
                .get()
            val elements = document.select("div.browse-thumbs-inner")
            val gifBeans = ArrayList<GifBean>()
            for (element in elements) {
                val gifBean = GifBean()
                gifBean.title = element.select("a")
                    .attr("title")
                gifBean.placeholder = "${Constant.BASE_URL}${element.select("img").attr("src")}"
                gifBean.gif =
                        "${gifBean.placeholder.substring(0, gifBean.placeholder.lastIndexOf("/") + 1
                        )}${gifBean.placeholder.substring(gifBean.placeholder.lastIndexOf("/") + 1).split(
                            "."
                        )[0].removeRange(0, 3
                        )}.webm"
                gifBeans.add(gifBean)
            }
            it.onNext(gifBeans)
        })
            .subscribeOn(Schedulers.io())
    }

    fun parseHtml(tag: String, page: Int): Observable<ArrayList<GifBean>> {
        return Observable.create(ObservableOnSubscribe<ArrayList<GifBean>> {
            val gifBeans = ArrayList<GifBean>()
            val dao = GifBaseHolder.getINSTANCE(Application.context)
                .dao
            val result = dao.getNextPage(tag, page)
            if (result == null) {
                if (StateUtil.isNetworkConnected()) {
                    try {
                        val url = "${Constant.HTML_BASE_URL}/$tag/$page"
                        val document = Jsoup.connect(url)
                            .get()
                        val elements = document.select("div.browse-thumbs-inner")
                        for (element in elements) {
                            val gifBean = GifBean()
                            gifBean.title = element.select("a")
                                .attr("title")
                            gifBean.placeholder =
                                    "${Constant.BASE_URL}${element.select("img").attr("src")}"
                            gifBean.gif = "${gifBean.placeholder.substring(0,
                                gifBean.placeholder.lastIndexOf("/") + 1
                            )}${gifBean.placeholder.substring(gifBean.placeholder.lastIndexOf("/") + 1).split(
                                "."
                            )[0].removeRange(0, 3
                            )}.webm"
                            gifBean.page = page
                            gifBeans.add(gifBean)
                        }
                        it.onNext(gifBeans)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    dao.insertNewPage(GifEntity(tag, page, toGson(gifBeans)))
                }
            } else {
                gifBeans.addAll(parseFromGson(result))
                it.onNext(gifBeans)
            }
        })
            .subscribeOn(Schedulers.io())
    }

    fun toGson(gifBeans: ArrayList<GifBean>): String {
        return gson.toJson(gifBeans)
    }

    fun parseFromGson(data: String): ArrayList<GifBean> {
        return gson.fromJson<ArrayList<GifBean>>(data)
    }
}

