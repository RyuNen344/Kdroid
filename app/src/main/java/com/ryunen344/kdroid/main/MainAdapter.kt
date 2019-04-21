package com.ryunen344.kdroid.main

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_main.view.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import twitter4j.Status

class MainAdapter(mainList: List<Status>, val mainItemListner: MainContract.MainItemListner, val utilProvider: UtilProvider) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var mainList : List<Status> = mainList
        set(mainList : List<Status>) {
            field = mainList
            notifyDataSetChanged()
        }
    private val SCREEN_NAME_PREFIX: String = "@"
    private val VIA_PREFIX: String = "via "
    private val HTML_VIA_PREFIX: String = "<html><head></head><body>"
    private val HTML_VIA_SUFIX: String = "</body></html>"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        return MainAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //set status bar
        if (mainList[position].isRetweet) {
            holder.main_color_bar.setBackgroundColor(Color.GREEN)
        } else {
            holder.main_color_bar.setBackgroundColor(Color.TRANSPARENT)
        }

        //set user name and screen name
        holder.main_account_name.text = mainList[position].user.name
        var mScreenName: String = SCREEN_NAME_PREFIX + mainList[position].user.screenName
        holder.main_screen_name.text = mScreenName

        if (mainList[position].user.isProtected) {
            holder.main_lock_icon.visibility = ImageView.VISIBLE
        } else {
            holder.main_lock_icon.visibility = ImageView.INVISIBLE
        }

        //set tweet detail
        holder.main_description.text = mainList[position].text

        //set via and date
        var doc: Document = Jsoup.parse(HTML_VIA_PREFIX + mainList[position].source + HTML_VIA_SUFIX)
        var mViaAndDate: String = VIA_PREFIX + doc.getElementsByTag("a").text() + " (" + utilProvider.provideSdf().format(mainList[position].createdAt) + ")"
        holder.main_via_and_date.text = mViaAndDate

        //set image icon
        Picasso.get()
                .load(mainList[position].user.biggerProfileImageURLHttps)
                .resize(50, 50)
                .into(holder.main_icon)

        holder.itemView.setOnClickListener { mainItemListner.onAccountClick() }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var main_color_bar: View = itemView.main_color_bar
        var main_icon: ImageView = itemView.main_icon
        var main_account_name: TextView = itemView.main_account_name
        var main_screen_name: TextView = itemView.main_screen_name
        var main_lock_icon: ImageView = itemView.main_lock_icon
        var main_via_and_date: TextView = itemView.main_via_and_date
        var main_description: TextView = itemView.main_description

    }
}