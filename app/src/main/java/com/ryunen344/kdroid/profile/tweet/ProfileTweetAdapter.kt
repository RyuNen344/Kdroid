package com.ryunen344.kdroid.profile.tweet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.di.provider.UtilProvider
import com.ryunen344.kdroid.util.debugLog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_main.view.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import twitter4j.Status

class ProfileTweetAdapter(profileTweetList: List<Status>, val profileItemListner: ProfileTweetContract.ProfileItemListner, val utilProvider: UtilProvider) : RecyclerView.Adapter<ProfileTweetAdapter.ViewHolder>() {

    var profileTweetList: List<Status> = profileTweetList
        set(profileTweetList: List<Status>) {
            field = profileTweetList
            notifyDataSetChanged()
        }

    private val SCREEN_NAME_PREFIX: String = "@"
    private val VIA_PREFIX: String = "via "
    private val HTML_VIA_PREFIX: String = "<html><head></head><body>"
    private val HTML_VIA_SUFIX: String = "</body></html>"

    private var mPicasso: Picasso = Picasso.get()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        return ProfileTweetAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileTweetList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //init picasso instance
        mPicasso.setIndicatorsEnabled(true)
        mPicasso.isLoggingEnabled = true

        //set status bar
        if (profileTweetList[position].isRetweet) {
            initTweet(holder, profileTweetList[position].retweetedStatus)

            //set image icon who retweeted
            holder.rt_icon.visibility = View.VISIBLE
            Picasso.get()
                    .load(profileTweetList[position].user.biggerProfileImageURLHttps)
                    .resize(23, 23)
                    .into(holder.rt_icon)

            holder.main_color_bar.setBackgroundColor(Color.GREEN)
        } else {
            initTweet(holder, profileTweetList[position])

            //set image icon who retweeted
            holder.rt_icon.visibility = View.INVISIBLE

            holder.main_color_bar.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemView.setOnClickListener { profileItemListner.onAccountClick() }
    }

    private fun initTweet(holder: ProfileTweetAdapter.ViewHolder, tweetStatus: Status) {
        //set user name and screen name
        holder.main_account_name.text = tweetStatus.user.name
        var mScreenName: String = SCREEN_NAME_PREFIX + tweetStatus.user.screenName
        holder.main_screen_name.text = mScreenName

        //set protect icon
        if (tweetStatus.user.isProtected) {
            holder.main_lock_icon.visibility = ImageView.VISIBLE
        } else {
            holder.main_lock_icon.visibility = ImageView.INVISIBLE
        }

        //set tweet detail
        holder.main_description.text = tweetStatus.text

        //set via and date
        var doc: Document = Jsoup.parse(HTML_VIA_PREFIX + tweetStatus.source + HTML_VIA_SUFIX)
        var mViaAndDate: String = VIA_PREFIX + doc.getElementsByTag("a").text() + " (" + utilProvider.provideSdf().format(tweetStatus.createdAt) + ")"
        holder.main_via_and_date.text = mViaAndDate


        //set image icon
        mPicasso
                .load(tweetStatus.user.biggerProfileImageURLHttps)
                .resize(50, 50)
                .placeholder(R.drawable.ic_loading_image_24dp)
                .error(R.drawable.ic_loading_image_24dp)
                .into(holder.main_icon)


        //set image
        if (tweetStatus.mediaEntities.isNotEmpty()) {
            debugLog("image load")
            debugLog("image url = " + tweetStatus.mediaEntities[0].mediaURLHttps)
            holder.main_image1.visibility = ImageView.VISIBLE
            mPicasso
                    .load(tweetStatus.mediaEntities[0].mediaURLHttps)
                    .placeholder(R.drawable.ic_loading_image_24dp)
                    .error(R.drawable.ic_loading_image_24dp)
                    .into(holder.main_image1)

            holder.main_image1.setOnClickListener {
                profileItemListner.onImageClick(tweetStatus.mediaEntities[0].mediaURLHttps)
            }


        } else {
            holder.main_image1.visibility = ImageView.GONE
        }
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