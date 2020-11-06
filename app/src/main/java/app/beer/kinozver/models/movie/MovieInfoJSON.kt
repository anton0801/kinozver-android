package app.beer.kinozver.models.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class MovieInfoJSON(
    @SerializedName("rus")
    @Expose
    var title: String = "",
    @SerializedName("year")
    @Expose
    var year: String = "",
    @SerializedName("time")
    @Expose
    var duration: String = "0",
    @SerializedName("poster")
    @Expose
    var posterUrl: String = "empty",
    @SerializedName("age")
    @Expose
    var age: String = "",
    @SerializedName("country")
    @Expose
    var country: String = "",
    @SerializedName("director")
    @Expose
    var director: String = "",
    @SerializedName("genre")
    @Expose
    var genre: String = "",
    @SerializedName("actors")
    @Expose
    var actors: String = "",
    @SerializedName("description")
    @Expose
    var description: String = "",
    var isNew: Boolean = year == SimpleDateFormat("yyyy").format(Date())
)