package app.beer.kinozver.models.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

open class MovieJSON(
    @SerializedName("id")
    @Expose
    var idM: String = "",
    @SerializedName("movie_id")
    @Expose
    var movie_id: String = "",
    @SerializedName("kinopoisk_id")
    @Expose
    var kp_id: String = "",
    @SerializedName("link")
    @Expose
    var videoUrl: String = "",
    @SerializedName("serial")
    @Expose
    var isSerial: String = "",
    @SerializedName("max_qual")
    @Expose
    var maxQual: String = "",
    @SerializedName("title")
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
    var isNew: Boolean = year == SimpleDateFormat("yyyy").format(Date()),
    @SerializedName("continueWatchTime")
    @Expose
    var continueWatchTime: Int = 0,
    @SerializedName("episode")
    @Expose
    var continueEpisode: Int = 0,
    @SerializedName("seria")
    @Expose
    var continueSeria: Int = 0
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MovieJSON

        if (idM != other.idM) return false
        if (movie_id != other.movie_id) return false
        if (kp_id != other.kp_id) return false
        if (videoUrl != other.videoUrl) return false
        if (isSerial != other.isSerial) return false
        if (maxQual != other.maxQual) return false
        if (title != other.title) return false
        if (year != other.year) return false
        if (duration != other.duration) return false
        if (posterUrl != other.posterUrl) return false
        if (age != other.age) return false
        if (country != other.country) return false
        if (director != other.director) return false
        if (genre != other.genre) return false
        if (actors != other.actors) return false
        if (description != other.description) return false
        if (isNew != other.isNew) return false
        if (continueWatchTime != other.continueWatchTime) return false
        if (continueEpisode != other.continueEpisode) return false
        if (continueSeria != other.continueSeria) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idM.hashCode()
        result = 31 * result + movie_id.hashCode()
        result = 31 * result + kp_id.hashCode()
        result = 31 * result + videoUrl.hashCode()
        result = 31 * result + isSerial.hashCode()
        result = 31 * result + maxQual.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + year.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + posterUrl.hashCode()
        result = 31 * result + age.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + director.hashCode()
        result = 31 * result + genre.hashCode()
        result = 31 * result + actors.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + isNew.hashCode()
        result = 31 * result + continueWatchTime
        result = 31 * result + continueEpisode
        result = 31 * result + continueSeria
        return result
    }

    override fun toString(): String {
        return "MovieJSON(id='$idM', movie_id='$movie_id', kp_id='$kp_id', videoUrl='$videoUrl', isSerial='$isSerial', maxQual='$maxQual', title='$title', year='$year', duration='$duration', posterUrl='$posterUrl', age='$age', country='$country', director='$director', genre='$genre', actors='$actors', description='$description', isNew=$isNew, continueWatchTime=$continueWatchTime, continueEpisode=$continueEpisode, continueSeria=$continueSeria)"
    }

}