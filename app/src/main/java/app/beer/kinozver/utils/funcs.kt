package app.beer.kinozver.utils

import android.widget.ImageView
import androidx.fragment.app.Fragment
import app.beer.kinozver.R
import app.beer.kinozver.database.*
import app.beer.kinozver.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
    if (addToBackStack) {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.data_container, fragment)
            .commit();
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(R.id.data_container, fragment)
            .commit();
    }
}

fun ImageView.downloadAndSetImage(url: String) {
    if (url != "empty") {
        Picasso.get()
            .load(url)
            .fit()
            .centerCrop()
            .into(this)
    }
}

fun initFirebase() {
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    AUTH = FirebaseAuth.getInstance()
    USER = User()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
}