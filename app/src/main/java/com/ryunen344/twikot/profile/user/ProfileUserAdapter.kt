package com.ryunen344.twikot.profile.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.twikot.R
import com.ryunen344.twikot.di.provider.AppProvider
import com.ryunen344.twikot.util.LogUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.User

class ProfileUserAdapter(profileUserList : List<User>, val profileItemListener : ProfileUserContract.ProfileItemListener) : RecyclerView.Adapter<ProfileUserAdapter.ViewHolder>(), KoinComponent {

    var profileUserList: List<User> = profileUserList
        set(profileUserList: List<User>) {
            field = profileUserList
            notifyDataSetChanged()
        }

    private val appProvider : AppProvider by inject()
    private var mPicasso: Picasso = appProvider.providePiccaso()

    companion object {
        private const val SCREEN_NAME_PREFIX : String = "@"
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        var view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ProfileUserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileUserList.size
    }

    override fun getItemId(position : Int) : Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        LogUtil.d()
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
        var screenName : String = Companion.SCREEN_NAME_PREFIX + profileUserList[position].screenName
        holder.user_screen_name.text = screenName
        holder.user_description.text = profileUserList[position].description

    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var user_icon : ImageView = itemView.user_icon
        var user_account_name : TextView = itemView.user_account_name
        var user_screen_name : TextView = itemView.user_screen_name
        var user_lock_icon : ImageView = itemView.user_lock_icon
        var user_description : TextView = itemView.user_profile_description

    }


}