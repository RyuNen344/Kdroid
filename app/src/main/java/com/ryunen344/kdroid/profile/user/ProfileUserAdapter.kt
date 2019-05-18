package com.ryunen344.kdroid.profile.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.di.provider.AppProvider
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.squareup.picasso.Picasso
<<<<<<< HEAD
import kotlinx.android.synthetic.main.item_user.view.*
=======
import kotlinx.android.synthetic.main.item_main.view.*
>>>>>>> parent of 9187e1f... profile画面を開くと落ちる問題を修正
import twitter4j.User

class ProfileUserAdapter(profileUserList: List<User>, val profileItemListner: ProfileUserContract.ProfileItemListner, val appProvider: AppProvider, val utilProvider: UtilProvider) : RecyclerView.Adapter<ProfileUserAdapter.ViewHolder>() {

    var profileUserList: List<User> = profileUserList
        set(profileUserList: List<User>) {
            field = profileUserList
            notifyDataSetChanged()
        }

    private val SCREEN_NAME_PREFIX : String = "@"

    private var mPicasso: Picasso = appProvider.providePiccaso()

<<<<<<< HEAD
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        var view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ProfileUserAdapter.ViewHolder(view)
=======
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
>>>>>>> parent of 9187e1f... profile画面を開くと落ちる問題を修正
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

<<<<<<< HEAD
    override fun getItemId(position : Int) : Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        debugLog("start")
        //set image icon
        mPicasso
                .load(profileUserList[position].biggerProfileImageURLHttps)
                .resize(50, 50)
                .placeholder(R.drawable.ic_loading_image_24dp)
                .error(R.drawable.ic_loading_image_24dp)
                .into(holder.user_icon)

        //set protect icon
        if (profileUserList[position].isProtected) {
            holder.user_lock_icon.visibility = ImageView.VISIBLE
        } else {
            holder.user_lock_icon.visibility = ImageView.INVISIBLE
        }

        holder.user_account_name.text = profileUserList[position].name
        var screenName : String = SCREEN_NAME_PREFIX + profileUserList[position].screenName
        holder.user_screen_name.text = screenName
        holder.user_description.text = profileUserList[position].description

        debugLog("end")
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var user_icon : ImageView = itemView.user_icon
        var user_account_name : TextView = itemView.user_account_name
        var user_screen_name : TextView = itemView.user_screen_name
        var user_lock_icon : ImageView = itemView.user_lock_icon
        var user_description : TextView = itemView.user_profile_description
=======
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
>>>>>>> parent of 9187e1f... profile画面を開くと落ちる問題を修正
    }
}