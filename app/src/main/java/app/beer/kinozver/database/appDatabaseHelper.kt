package app.beer.kinozver.database

import app.beer.kinozver.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var CURRENT_UID: String
lateinit var USER: User

const val NODE_USERS = "users"
const val NODE_PHOTOS = "profile_images"

const val CHILD_ID = "id"
const val CHILD_FULL_NAME = "full_name"
const val CHILD_EMAIL = "email"
const val CHILD_STATE = "state"
const val CHILD_AVATAR = "avatar"


