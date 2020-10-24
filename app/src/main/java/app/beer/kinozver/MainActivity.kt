package app.beer.kinozver

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import app.beer.kinozver.database.*
import app.beer.kinozver.ui.fragments.main.MainFragment
import app.beer.kinozver.ui.fragments.account.AccountFragment
import app.beer.kinozver.ui.fragments.register.RegisterFragment
import app.beer.kinozver.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_account.*

class MainActivity : AppCompatActivity() {

    lateinit var main_toolbar: Toolbar
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        APP_ACTIVITY = this
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
                R.id.nav_account -> {
                    replaceFragment(AccountFragment())
                }
            }
            true
        }
    }

}