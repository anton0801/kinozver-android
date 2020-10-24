package app.beer.kinozver.models

data class User(
    var id: String = "",
    var userName: String = "",
    var avatarUrl: String = "empty",
    var email: String = "",
    var password: String = ""
) {
    override fun toString(): String {
        return "User(id='$id', userName='$userName', avatarUrl='$avatarUrl', email='$email', password='$password')"
    }
}