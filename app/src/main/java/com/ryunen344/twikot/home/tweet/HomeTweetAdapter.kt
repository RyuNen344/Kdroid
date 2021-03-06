package com.ryunen344.twikot.home.tweet

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ryunen344.twikot.R
import com.ryunen344.twikot.di.provider.AppProvider
import com.ryunen344.twikot.di.provider.UtilProvider
import com.ryunen344.twikot.util.LogUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_tweet.view.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.koin.core.KoinComponent
import org.koin.core.inject
import twitter4j.Status
import java.util.regex.Pattern


class HomeTweetAdapter(mainList : MutableList<Status>, private val tweetItemListener : HomeTweetContract.TweetItemListener) : RecyclerView.Adapter<HomeTweetAdapter.ViewHolder>(), KoinComponent {

    var tweetList : MutableList<Status> = mainList
        set(mainList : MutableList<Status>) {
            field = mainList
            notifyDataSetChanged()
        }

    private val appProvider : AppProvider by inject()
    private val utilProvider : UtilProvider by inject()

    private val SCREEN_NAME_PREFIX : String = "@"
    private val VIA_PREFIX : String = "via "
    private val HTML_VIA_PREFIX : String = "<html><head></head><body>"
    private val HTML_VIA_SUFIX : String = "</body></html>"
    private val SCREEN_NAME_PATTERN = Pattern.compile("@([A-Za-z0-9_-]+)")
    private val HASH_TAG_PATTERN = Pattern.compile("#([A-Za-z0-9_-]+)")

    var position : Int = -1
    var mUserId : Long = 0L

    private var mPicasso : Picasso = appProvider.providePiccaso()

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        var view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
        return HomeTweetAdapter.ViewHolder(view)
    }

    override fun getItemCount() : Int {
        return tweetList.size
    }

    override fun getItemId(position : Int) : Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        LogUtil.d()

        //set status bar
        if (tweetList[position].isRetweet && !tweetList[position].isRetweetedByMe) {
            initTweet(holder, tweetList[position].retweetedStatus)

            //set image icon who retweeted
            holder.rt_icon.visibility = View.VISIBLE
            mPicasso.load(tweetList[position].user.biggerProfileImageURLHttps)
                    .resize(23, 23)
                    .into(holder.rt_icon)

            holder.tweet_color_bar.setBackgroundColor(Color.GREEN)

        } else {
            initTweet(holder, tweetList[position])

            //set image icon who retweeted
            holder.rt_icon.visibility = View.INVISIBLE

            if (tweetList[position].user.id == mUserId) {
                holder.tweet_color_bar.setBackgroundColor(Color.GRAY)
            } else {
                holder.tweet_color_bar.setBackgroundColor(Color.TRANSPARENT)
            }

            mPicasso.load(tweetList[position].user.biggerProfileImageURLHttps)
                    .resize(23, 23)
                    .into(holder.rt_icon)

        }

        holder.tweet_context_menu.setOnClickListener {
            this.position = position
            tweetItemListener.onContextMenuClick(position, tweetList[position])
        }

        holder.itemView.setOnClickListener {
            tweetItemListener.onTweetClick(tweetList[position])
        }

        holder.itemView.setOnLongClickListener {
            tweetItemListener.onTweetLongClick(position, tweetList[position])
            true
        }

        holder.tweet_description.setOnClickListener {
            tweetItemListener.onTweetClick(tweetList[position])
        }

        holder.tweet_description.setOnLongClickListener {
            tweetItemListener.onTweetLongClick(position, tweetList[position])
            true
        }
    }

    private fun initTweet(holder : ViewHolder, tweetStatus : Status) {
        //set user name and screen name
        holder.tweet_account_name.text = tweetStatus.user.name
        var mScreenName : String = SCREEN_NAME_PREFIX + tweetStatus.user.screenName
        holder.tweet_screen_name.text = mScreenName

        //set protect icon
        if (tweetStatus.user.isProtected) {
            holder.tweet_lock_icon.visibility = ImageView.VISIBLE
        } else {
            holder.tweet_lock_icon.visibility = ImageView.INVISIBLE
        }

        //set fav icon
        if (tweetStatus.isFavorited) {
            holder.tweet_fav_icon.visibility = ImageView.VISIBLE
        } else {
            holder.tweet_fav_icon.visibility = ImageView.INVISIBLE
        }

        //set rt icon
        if (tweetStatus.isRetweetedByMe) {
            holder.tweet_retweet_icon.visibility = ImageView.VISIBLE
        } else {
            holder.tweet_retweet_icon.visibility = ImageView.INVISIBLE
        }

        //set tweet detail
        //holder.tweet_description.text = tweetStatus.text
        //tweetStatus.inReplyToUserId
        initDescription(holder.tweet_description, tweetStatus)

        //set via and date
        var doc : Document = Jsoup.parse(HTML_VIA_PREFIX + tweetStatus.source + HTML_VIA_SUFIX)
        var mViaAndDate : String = VIA_PREFIX + doc.getElementsByTag("a").text() + " (" + utilProvider.provideSdf().format(tweetStatus.createdAt) + ")"
        holder.tweet_via_and_date.text = mViaAndDate


        //set image icon
        mPicasso.load(tweetStatus.user.biggerProfileImageURLHttps)
                .resize(50, 50)
                .placeholder(R.drawable.ic_loading_image_24dp)
                .error(R.drawable.ic_loading_image_24dp)
                .into(holder.tweet_icon)
        holder.tweet_icon.setOnClickListener {
            tweetItemListener.onAccountClick(tweetStatus.user)
        }

        initImage(holder, tweetStatus)
    }

    private fun initDescription(textView : TextView, tweetStatus : Status) {

        var spannableString = SpannableString(tweetStatus.text)
        val replyMatcher = SCREEN_NAME_PATTERN.matcher(tweetStatus.text)
        val hashTagMatcher = HASH_TAG_PATTERN.matcher(tweetStatus.text)

        while (replyMatcher.find()) {
            var start = replyMatcher.start()
            var end = replyMatcher.end()

            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(textView : View) {
                    LogUtil.d(spannableString.substring(start + 1, end))
                    tweetItemListener.onAccountClick(spannableString.substring(start + 1, end))
                }
            }, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }

        while (hashTagMatcher.find()) {
            var start = hashTagMatcher.start()
            var end = hashTagMatcher.end()

            spannableString.setSpan(object : ClickableSpan() {
                override fun onClick(textView : View) {
                    LogUtil.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! onclick きいてるで！！！ハッシュタグ！！")
                }
            }, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }

        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()

    }


    private fun initImage(holder : ViewHolder, tweetStatus : Status) {

        //set visible
        holder.imageFrame.visibility = View.GONE
        for (i in 0 until 4) {
            holder.imageList[i].visibility = View.GONE
        }

        if (tweetStatus.mediaEntities.isNotEmpty()) {
            holder.imageFrame.visibility = View.VISIBLE
            for (i in tweetStatus.mediaEntities.indices) {
                setImage(holder.imageList[i], tweetStatus.mediaEntities[i].mediaURLHttps)
            }
        }


    }

    private fun setImage(imageView : ImageView, mediaUrl : String) {

        //set image
        LogUtil.d("image load")
        LogUtil.d("image url = " + mediaUrl)
        imageView.visibility = ImageView.VISIBLE
        mPicasso.load(mediaUrl)
                .placeholder(R.drawable.ic_loading_image_24dp)
                .error(R.drawable.ic_loading_image_24dp)
                .into(imageView)

        imageView.setOnClickListener {
            tweetItemListener.onImageClick(mediaUrl, it as ImageView)
        }
    }

    fun notifyStatusChange(position : Int, tweet : Status) {
        LogUtil.d()
        tweetList[position] = tweet
        notifyItemChanged(position)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var tweet_color_bar : View = itemView.tweet_color_bar
        var tweet_icon : ImageView = itemView.tweet_icon
        var rt_icon : ImageView = itemView.rt_icon
        var tweet_account_name : TextView = itemView.tweet_account_name
        var tweet_screen_name : TextView = itemView.tweet_screen_name
        var tweet_lock_icon : ImageView = itemView.tweet_lock_icon
        var tweet_fav_icon : ImageView = itemView.tweet_fav_icon
        var tweet_retweet_icon : ImageView = itemView.tweet_retweet_icon
        var tweet_via_and_date : TextView = itemView.tweet_via_and_date
        var tweet_description : TextView = itemView.tweet_description
        var tweet_context_menu : ImageView = itemView.tweet_context_menu
        var imageFrame : View = itemView.imageFrame
        var imageList : List<ImageView> = listOf<ImageView>(itemView.tweet_image1, itemView.tweet_image2, itemView.tweet_image3, itemView.tweet_image4)
    }
}