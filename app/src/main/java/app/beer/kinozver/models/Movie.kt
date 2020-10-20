package app.beer.kinozver.models

import java.io.Serializable

data class Movie(
    var  id: Int = 1,
    var name: String = "",
    var posterUrl: String = "empty"
) : Serializable