package app.beer.kinozver.database

import android.net.Uri
import app.beer.kinozver.R
import app.beer.kinozver.models.user.User
import app.beer.kinozver.models.user.UserResponse
import app.beer.kinozver.ui.fragments.main.MainFragment
import app.beer.kinozver.ui.fragments.register.RegisterFragment
import app.beer.kinozver.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var CURRENT_UID: String
lateinit var USER: User

const val NODE_USERS = "users"
const val NODE_EMAIL = "emails"

const val CHILD_ID = "id"
const val CHILD_FULL_NAME = "userName"
const val CHILD_EMAIL = "email"
const val CHILD_STATE = "state"
const val CHILD_AVATAR = "avatarUrl"
const val CHILD_PASSWORD = "password"

const val FOLDER_USER_IMAGES = "user_images"

fun initUser(id: Int) {
    APP.getMovieApi().getUserById(id).enqueue(AppRetrofitCallback<UserResponse> { _, response ->
        val userResponse = response.body()
        if (userResponse != null) {
            if (userResponse.message == "Такого пользователя не существует") {
                showToast("Такого пользователя не существует")
                replaceFragment(RegisterFragment())
            } else {
                USER = userResponse.user
                replaceFragment(MainFragment())
            }
        }
    })
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline onSuccess: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener {
            showToast(APP_ACTIVITY.getString(R.string.error_some_went).format(it.message))
        }
}

inline fun putUrlToDatabase(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS)
        .child(CURRENT_UID)
        .child(CHILD_AVATAR)
        .setValue(url)
        .addOnSuccessListener {
            function()
        }
        .addOnFailureListener {
            showToast(
                APP_ACTIVITY.getString(R.string.error_some_went).format(it.message)
            )
        }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener {
            showToast(
                APP_ACTIVITY.getString(R.string.error_some_went).format(it.message)
            )
        }
}

