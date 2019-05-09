package com.ryunen344.kdroid.profile.user

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.kdroid.di.provider.UtilProvider
import kotlinx.android.synthetic.main.item_main.view.*
import twitter4j.Status

class ProfileUserAdapter(mainList: List<Status>, val profileItemListner: ProfileUserContract.ProfileItemListner, val utilProvider: UtilProvider) : RecyclerView.Adapter<ProfileUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var main_color_bar: View = itemView.main_color_bar
        var main_icon: ImageView = itemView.main_icon
        var rt_icon: ImageView = itemView.rt_icon
        var main_account_name: TextView = itemView.main_account_name
        var main_screen_name: TextView = itemView.main_screen_name
        var main_lock_icon: ImageView = itemView.main_lock_icon
        var main_via_and_date: TextView = itemView.main_via_and_date
        var main_description: TextView = itemView.main_description
        var main_image1: ImageView = itemView.main_image1
        var main_image2: ImageView = itemView.main_image2
        var main_image3: ImageView = itemView.main_image3
        var main_image4: ImageView = itemView.main_image4
    }
}