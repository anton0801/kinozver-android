package app.beer.kinozver.utils

import app.beer.kinozver.models.movie.MovieJSON
import app.beer.kinozver.models.movie.MovieValue
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("json?type=all")
    fun getMoviesAll(@Query("page") page: Int): Call<MovieValue>

    @GET("json")
    fun getMoviesByType(@Query("type") type: String, @Query("page") page: Int): Call<MovieValue>

    @GET("json")
    fun getMovieByCategory(@Query("type") type: String, @Query("page") page: Int, @Query("cat") cat: String) : Call<MovieValue>

    @GET("json?type=all&page=1")
    fun getMovieByCategory(@Query("cat") cat: String) : Call<MovieValue>

    @GET("search")
    fun getMoviesBySearch(@Query("title") title: String) : Call<MovieValue>

    @GET("search")
    fun getMovieById(@Query("kp") kp: String): Call<MovieValue>

}