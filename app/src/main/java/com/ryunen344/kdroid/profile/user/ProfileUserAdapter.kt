package com.ryunen344.kdroid.profile.user

import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.util.debugLog
import com.squareup.picasso.Picasso
import twitter4j.User

class ProfileUserAdapter(profileUserList : List<User>, val profileItemListner : ProfileUserContract.ProfileItemListner, val appProvider : AppProvider, val utilProvider : UtilProvider) : RecyclerView.Adapter<ProfileUserAdapter.ViewHolder>() {

    var profileUserList : List<User> = profileUserList
        set(profileUserList : List<User>) {
            field = profileUserList
            notifyDataSetChanged()
        }


    private var mPicasso : Picasso = appProvider.providePiccaso()

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        //var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        //return ProfileUserAdapter.ViewHolder(view)
        return ViewHolder(parent.get(viewType))
    }

    override fun getItemCount() : Int {
        return profileUserList.size
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        debugLog("start")
        debugLog("end")
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
//        var main_color_bar: View = itemView.main_color_bar
//        var main_icon: ImageView = itemView.main_icon
//        var rt_icon: ImageView = itemView.rt_icon
//        var main_account_name: TextView = itemView.main_account_name
//        var main_screen_name: TextView = itemView.main_screen_name
//        var main_lock_icon: ImageView = itemView.main_lock_icon
//        var main_via_and_date: TextView = itemView.main_via_and_date
//        var main_description: TextView = itemView.main_description
//        var main_image1: ImageView = itemView.main_image1
//        var main_image2: ImageView = itemView.main_image2
//        var main_image3: ImageView = itemView.main_image3
//        var main_image4: ImageView = itemView.main_image4
    }
}