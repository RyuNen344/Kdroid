package com.ryunen344.twikot.settings.preferences.license

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ryunen344.twikot.R.layout.fragment_license_text
import com.ryunen344.twikot.util.LogUtil

class LicenseTextFragment : Fragment() {

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        LogUtil.d()
        var root : View = inflater.inflate(fragment_license_text, container, false)



        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onActivityCreated(savedInstanceState)
    }

}