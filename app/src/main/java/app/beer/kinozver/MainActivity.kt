package app.beer.kinozver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import app.beer.kinozver.database.*
import app.beer.kinozver.ui.fragments.main.MainFragment
import app.beer.kinozver.ui.fragments.account.AccountFragment
import app.beer.kinozver.ui.fragments.map.PermissionFragment
import app.beer.kinozver.ui.fragments.register.RegisterFragment
import app.beer.kinozver.ui.fragments.search.SearchFragment
import app.beer.kinozver.ui.objects.MovieLifecycle
import app.beer.kinozver.ui.viewmodel.MovieViewModel
import app.beer.kinozver.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var main_toolbar: Toolbar
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var movieLifecycle: MovieLifecycle
    lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieLifecycle = MovieLifecycle()
        lifecycle.addObserver(movieLifecycle)
        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        APP_ACTIVITY = this
        APP = App()
        initFirebase()
        initFields()
        if (AUTH.currentUser != null) {
            initUser()
            replaceFragment(MainFragment())
        } else {
            replaceFragment(RegisterFragment())
        }
    }

    private fun initFields() {
        main_toolbar = toolbar
        setSupportActionBar(main_toolbar)
        bottomNavigationView = bottom_nav
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    replaceFragment(MainFragment())
                }
                R.id.nav_movies -> {
                    replaceFragment(SearchFragment())
                }
                R.id.nav_map -> {
                    replaceFragment(PermissionFragment())
                }
                R.id.nav_account -> {
                    replaceFragment(AccountFragment())
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }

}