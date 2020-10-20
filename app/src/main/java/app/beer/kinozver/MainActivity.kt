package app.beer.kinozver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import app.beer.kinozver.database.AUTH
import app.beer.kinozver.models.Movie
import app.beer.kinozver.ui.fragments.Main.MainFragment
import app.beer.kinozver.ui.fragments.show.MovieDetailFragment
import app.beer.kinozver.ui.fragments.show.ShowMovieFragment
import app.beer.kinozver.utils.APP_ACTIVITY
import app.beer.kinozver.utils.initFirebase
import app.beer.kinozver.utils.replaceFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    public lateinit var main_toolbar: Toolbar
    public lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        APP_ACTIVITY = this
        replaceFragment(MainFragment())
        initFirebase()
        initFields()
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
            }
            true
        }
    }

}