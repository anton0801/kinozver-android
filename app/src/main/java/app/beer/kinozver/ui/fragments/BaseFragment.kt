package app.beer.kinozver.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.beer.kinozver.utils.APP_ACTIVITY

open class BaseFragment(resId: Int) : Fragment(resId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        APP_ACTIVITY.supportActionBar?.show()
    }

}