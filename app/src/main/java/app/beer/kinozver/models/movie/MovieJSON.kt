package app.beer.kinozver.models.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieJSON(
    @SerializedName("kinopoisk_id")
    @Expose
    var id: String = "",
    @SerializedName("info")
    @Expose
    var info: MovieInfoJSON = MovieInfoJSON(),
    @SerializedName("link")
    @Expose
    var videoUrl: String = "",
    @SerializedName("serial")
    @Expose
    var isSerial: String = "",
    @SerializedName("max_qual")
    @Expose
    var maxQual: String = ""
)