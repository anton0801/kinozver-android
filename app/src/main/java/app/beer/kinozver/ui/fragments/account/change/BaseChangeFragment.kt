package app.beer.kinozver.ui.fragments.account.change

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import app.beer.kinozver.R
import app.beer.kinozver.ui.fragments.account.AccountFragment
import app.beer.kinozver.utils.APP_ACTIVITY
import app.beer.kinozver.utils.replaceFragment
import kotlinx.android.synthetic.main.activity_main.*

open class BaseChangeFragment(resId: Int, val title: String) : Fragment(resId) {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        APP_ACTIVITY.toolbar.title = title
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY.toolbar.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
    }

    open fun change() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.change_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_change) {
            change()
        }
        return true
    }

}