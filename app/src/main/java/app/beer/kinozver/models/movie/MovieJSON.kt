package app.beer.kinozver.models.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MovieJSON(
    var  id: Int = 1,
    var name: String = "",
    var posterUrl: String = "empty",
    @SerializedName("")
    @Expose
    var media: ArrayList<MovieMediaJSON> = arrayListOf(),
    @SerializedName("iframe_src")
    @Expose
    var videoUrl: String = "",
    @SerializedName("year")
    @Expose
    var year: String = ""
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MovieJSON

        if (id != other.id) return false
        if (name != other.name) return false
        if (posterUrl != other.posterUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + posterUrl.hashCode()
        return result
    }
}