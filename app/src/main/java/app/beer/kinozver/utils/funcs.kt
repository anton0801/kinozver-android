package app.beer.kinozver.utils

import android.content.Intent
import android.content.pm.PackageManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import app.beer.kinozver.MainActivity
import app.beer.kinozver.R
import app.beer.kinozver.database.*
import app.beer.kinozver.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.regex.Pattern

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

fun restartActivity() {
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.finish()
    APP_ACTIVITY.startActivity(intent)
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

fun ImageView.downloadAndSetUserImage(url: String) {
    if (url != "empty") {
        Picasso.get()
            .load(url)
            .fit()
            .centerCrop()
            .error(R.drawable.user_default)
            .placeholder(R.drawable.user_default)
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

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun isEmailValid(email: String): Boolean {
    val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(email)
    return matcher.matches()
}

fun checkPermission(permission: String, requestCode: Int): Boolean {
    if (ActivityCompat.checkSelfPermission(APP_ACTIVITY, permission) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), requestCode)
        return false
    }
    return true
}

fun checkEmailInDatabase(email: String) {
    AUTH.fetchSignInMethodsForEmail(email)
        .addOnCompleteListener {
        }
}