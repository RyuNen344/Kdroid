package com.ryunen344.kdroid.main

import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.ryunen344.kdroid.R
import com.ryunen344.kdroid.util.ensureNotNull
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : Fragment(), MainContract.View{

    var mPresenter : MainContract.Presenter? = null
    var mTitle : TextView? = null
    var mDescription : TextView? = null
    var mCheckBox : CheckBox? = null

    companion object {
        fun newInstance() : MainFragment{
            return MainFragment()
        }
    }

    fun newInstance() : MainFragment {
        val fragment = MainFragment()
        return fragment
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.start()
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        var root : View = inflater.inflate(R.layout.fragment_main,container,false)
        setHasOptionsMenu(true)
        mTitle = root.frag_title
        mDescription = root.frag_description
        mCheckBox = root.checkBox_complete


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
                    activity?.messageM?.setText(R.string.title_home)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    activity?.messageM?.setText(R.string.title_dashboard)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    activity?.messageM?.setText(R.string.title_notifications)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        return root
    }

    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater) {
        inflater.inflate(R.menu.navigation, menu)
    }

    override fun isActive() : Boolean {
        return isAdded
    }

    override fun showTitle(title : String) {
        mTitle?.visibility = View.VISIBLE
        mTitle?.text = title
    }

    override fun showDescription(description : String) {
        mDescription?.visibility = View.VISIBLE
        mDescription?.text = description
    }

    override fun setPresenter(presenter : MainContract.Presenter) {
        ensureNotNull(presenter){ p ->
            mPresenter = p
        }
    }



}