package app.beer.kinozver

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import app.beer.kinozver.database.initUser
import app.beer.kinozver.ui.fragments.account.AccountFragment
import app.beer.kinozver.ui.fragments.main.MainFragment
import app.beer.kinozver.ui.fragments.map.PermissionFragment
import app.beer.kinozver.ui.fragments.register.RegisterFragment
import app.beer.kinozver.ui.fragments.search.SearchFragment
import app.beer.kinozver.ui.fragments.splash.SplashFragment
import app.beer.kinozver.ui.fragments.subscribe.SubscribeFragment
import app.beer.kinozver.ui.viewmodel.MovieViewModel
import app.beer.kinozver.utils.*
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

const val SUBSCRIBE_COUNTER_KEY = "subscribe_counter"

const val IS_AUTH_KEY = "is_auth"
const val USER_ID_KEY = "user_id"
const val DEFAULT_USER_AVATAR_URL = "https://www.skolahaj.cz/wp-content/uploads/2016/12..."

class MainActivity : AppCompatActivity() {

    lateinit var mainToolbar: Toolbar
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var viewModel: MovieViewModel

    private lateinit var sharedManager: SharedManager

    // for the open window with offer for bay subscription for delete ads from movie
    private var counter = 0

    /**
     * TODO: Сделать подписку чтобы убрать рекламу при просмотре фильма, подписка должна стоять 159 рублей в месяц, оплата через google play!
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        APP_ACTIVITY = this
        APP = application as App
        sharedManager = SharedManager()

        replaceFragment(SplashFragment())

        // initialize mobile ads
        MobileAds.initialize(this) {
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        initFirebase()
        initFields()
        if (sharedManager.getBoolean(IS_AUTH_KEY)) {
            initUser(sharedManager.getInt(USER_ID_KEY))
        } else {
            replaceFragment(RegisterFragment())
        }

        counter = sharedManager.getInt(SUBSCRIBE_COUNTER_KEY)
        sharedManager.putInt(SUBSCRIBE_COUNTER_KEY, counter++)
    }

    private fun initFields() {
        mainToolbar = toolbar
        setSupportActionBar(mainToolbar)
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

    override fun onStart() {
        super.onStart()
        if (counter == 5) {
            sharedManager.putInt(SUBSCRIBE_COUNTER_KEY, 0)
            counter = 0
        }
        if (counter == 0) {
            replaceFragment(SubscribeFragment(), true)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack()
        else
            super.onBackPressed()
    }

}