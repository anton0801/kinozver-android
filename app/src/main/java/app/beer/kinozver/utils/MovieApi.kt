package app.beer.kinozver.utils

import app.beer.kinozver.models.movie.MovieJSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movies?ordering=created&direction=desc")
    fun getMovies(@Query("page") page: Int): Call<MovieJSON>

    @GET("N3dMPlk0C1Dl/movie/{id}")
    fun getMovieById(@Path("id") id: Int): Call<MovieJSON>

}