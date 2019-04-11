package com.ryunen344.kdroid.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.kdroid.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_main.view.*
import twitter4j.Status

class MainAdapter(mainList: List<Status>, val mainItemListner: MainContract.MainItemListner) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {


    var mainList : List<Status> = mainList
        set(mainList : List<Status>) {
            field = mainList
            notifyDataSetChanged()
        }

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
        holder.main_account_name.text = mainList[position].user.name
        holder.main_screen_name.text = mainList[position].user.screenName
        holder.main_description.text = mainList[position].text
        Picasso.get()
                .load(mainList[position].user.biggerProfileImageURLHttps)
                .resize(30, 30)
                .into(holder.main_icon)

        holder.itemView.setOnClickListener { mainItemListner.onAccountClick() }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var main_icon: ImageView = itemView.main_icon
        var main_account_name: TextView = itemView.main_account_name
        var main_screen_name: TextView = itemView.main_screen_name
        var main_description: TextView = itemView.main_description

    }
}