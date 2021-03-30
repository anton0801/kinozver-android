package app.beer.kinozver.models.user

import com.google.gson.annotations.SerializedName

const val IS_BANNED_TRUE = 1
const val IS_BANNED_FALSE = 0

const val ROLE_ADMIN = 1
const val ROLE_USER = 2

data class User(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var userName: String = "",
    @SerializedName("avatarUrl")
    var avatarUrl: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("to_time_premium")
    var toTimePremium: String = "",
    @SerializedName("is_banned")
    var is_banned: Int = IS_BANNED_FALSE,
    @SerializedName("role_id")
    var roleId: Int = ROLE_USER
)