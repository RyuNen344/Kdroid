package com.ryunen344.kdroid.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ryunen344.kdroid.R
import kotlinx.android.synthetic.main.item_main.view.*
import twitter4j.Status

class MainAdapter(mainList : List<Status>, val mainItemListner : MainContract.MainItemListner) : BaseAdapter() {

    var mainList : List<Status> = mainList
        set(mainList : List<Status>) {
            field = mainList
            notifyDataSetChanged()
        }

    override fun getView(position : Int, convertView : View?, parent : ViewGroup) : View {
        val main : Status = getItem(position)
        var view : View? = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
            holder = ViewHolder(view.main_icon, view.main_account_name, view.main_screen_name,view.main_description)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        holder.main_account_name.text = main.user.name
        holder.main_screen_name.text = main.user.screenName
        holder.main_description.text = main.text

//        val rowView = convertView ?: LayoutInflater.from(parent.context)
//                .inflate(fragment_account_list, parent, false)
        view!!.setOnClickListener { mainItemListner.onAccountClick() }
        return view
    }

    override fun getItem(position : Int) : Status {
        return mainList[position]
    }

    override fun getItemId(position : Int) : Long {
        return mainList[position].id
    }

    override fun getCount() : Int {
        return mainList.size
    }

    data class ViewHolder(var main_icon : ImageView, var main_account_name : TextView,var main_screen_name : TextView, var main_description : TextView)
}