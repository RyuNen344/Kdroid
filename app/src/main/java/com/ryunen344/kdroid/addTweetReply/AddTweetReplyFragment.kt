package com.ryunen344.kdroid.addTweetReply

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.util.LogUtil
import kotlinx.android.synthetic.main.activity_add_tweet_reply.*
import kotlinx.android.synthetic.main.fragment_add_tweet_reply.*
import kotlinx.android.synthetic.main.fragment_add_tweet_reply.view.*
import org.koin.android.scope.currentScope

class AddTweetReplyFragment : Fragment(), AddTweetReplyContract.View {

    override val presenter : AddTweetReplyContract.Presenter by currentScope.inject()

    var mTweetDescription : TextView? = null
    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
    }


    override fun onActivityCreated(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onActivityCreated(savedInstanceState)

        activity?.tweetButton?.setOnClickListener {
            presenter.sendTweet(mTweetDescription?.text.toString())
        }

    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        LogUtil.d()
        var root : View = inflater.inflate(R.layout.fragment_add_tweet_reply, container, false)
        with(root) {
            mTweetDescription = this.add_tweet_reply_description
        }
        setHasOptionsMenu(true)
        return root
    }

    override fun showTimeline() {
        LogUtil.d()
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun showError(e : Throwable) {
        //fixme
        LogUtil.e(e)
        Snackbar.make(add_tweet_reply_description, e.message.toString(), Snackbar.LENGTH_LONG).show()
    }


}