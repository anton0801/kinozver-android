package app.beer.kinozver.models.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieMediaJSON(
    @SerializedName("content_type")
    @Expose
    var contentType: String = "",
    @SerializedName("max_quality")
    @Expose
    var maxQuality: Int = 0,
    @SerializedName("max_quality")
    @Expose
    var duration: Int = 0
)