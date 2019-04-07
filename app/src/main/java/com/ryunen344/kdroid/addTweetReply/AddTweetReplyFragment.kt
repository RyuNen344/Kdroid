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
import com.ryunen344.kdroid.util.debugLog
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.activity_add_tweet_reply.*
import kotlinx.android.synthetic.main.fragment_add_tweet_reply.*
import kotlinx.android.synthetic.main.fragment_add_tweet_reply.view.*

class AddTweetReplyFragment : Fragment(), AddTweetReplyContract.View {

    lateinit var mPresenter : AddTweetReplyContract.Presenter
    var mTweetDescription : TextView? = null

    companion object {
        fun newInstance() = AddTweetReplyFragment()
    }

    override fun setPresenter(presenter : AddTweetReplyContract.Presenter) {
        debugLog("start")
        ensureNotNull(presenter) { p ->
            mPresenter = p
        }
        debugLog("end")
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.tweetButton?.setOnClickListener {
            mPresenter.sendTweet(mTweetDescription?.text.toString())
        }

    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        debugLog("start")
        var root : View = inflater.inflate(R.layout.fragment_add_tweet_reply, container, false)
        with(root) {
            mTweetDescription = this.add_tweet_reply_description
        }
        setHasOptionsMenu(true)
        debugLog("end")
        return root
    }

    override fun showTimeline() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun showError(e : Throwable) {
        //fixme
        debugLog("start")
        debugLog(e.message.toString())
        Snackbar.make(add_tweet_reply_description, e.message.toString(), Snackbar.LENGTH_LONG).show()
        debugLog("end")
    }


}