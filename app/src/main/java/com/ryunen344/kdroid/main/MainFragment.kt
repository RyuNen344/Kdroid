package com.ryunen344.kdroid.main

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.activity_account_lsit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import twitter4j.Status

class MainFragment : Fragment(), MainContract.View{


    lateinit var mPresenter : MainContract.Presenter
    lateinit var mainListView : LinearLayout

    companion object {
        fun newInstance() = MainFragment()
    }


    private var itemListener: MainContract.MainItemListner = object : MainContract.MainItemListner {
        override fun onAccountClick() {
            //fixme
            println("open timeline")
        }
    }

    private val mainAdapter = MainAdapter(ArrayList(0), itemListener)

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        var root : View = inflater.inflate(R.layout.fragment_main,container,false)

        with(root){
            val listView = main_list.apply {
                adapter = mainAdapter
            }

            mainListView = mainLL

            activity?.account_fab?.setOnClickListener {
                mPresenter.openTweetDetail()
            }
        }
        setHasOptionsMenu(true)

        //configure float action button
        activity?.fab?.setOnLongClickListener { view ->
            Snackbar.make(view, "Long tap action", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            true
        }
        activity?.fab?.setOnClickListener { view ->
            Snackbar.make(view, "Single tap action", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
        }

        //configure navigation bar
        activity?.navigation!!.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        main_list.adapter = mainAdapter
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun showTweetList(mainList : List<Status>) {
        mainAdapter.mainList = mainList
        mainAdapter.notifyDataSetChanged()
    }


    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater) {
        inflater.inflate(R.menu.navigation, menu)
    }

    override fun setPresenter(presenter : MainContract.Presenter) {
        ensureNotNull(presenter){ p ->
            mPresenter = p
        }
    }



}