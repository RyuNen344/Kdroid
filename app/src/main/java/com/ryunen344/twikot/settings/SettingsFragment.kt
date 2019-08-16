package com.ryunen344.twikot.settings

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ryunen344.twikot.R.xml.pref_root
import com.ryunen344.twikot.settings.preferences.WallpaperPreference
import com.ryunen344.twikot.settings.preferences.WallpaperPreferenceDialogFragmentCompat
import com.ryunen344.twikot.util.LogUtil
import org.koin.android.scope.currentScope

class SettingsFragment : PreferenceFragmentCompat(), SettingsContract.View {

    override val presenter : SettingsContract.Presenter by currentScope.inject()

    companion object {
        const val DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG"
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        LogUtil.d()
        super.onCreate(savedInstanceState)
        presenter.view = this
    }

    override fun onCreatePreferences(savedInstanceState : Bundle?, rootKey : String?) {
        LogUtil.d()
        setPreferencesFromResource(pref_root, rootKey)
    }

    override fun doSomething() {
        LogUtil.d("something display!!!!!!!!!")
    }

    override fun showError(e : Throwable) {
        LogUtil.e(e)
    }

    override fun onDisplayPreferenceDialog(preference : Preference?) {
        LogUtil.d()

        //handling custom dialog
        if (preference is WallpaperPreference) {
            var dialogFragment : DialogFragment = WallpaperPreferenceDialogFragmentCompat.newInstance(preference.key)
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(fragmentManager!!, DIALOG_FRAGMENT_TAG)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

}
