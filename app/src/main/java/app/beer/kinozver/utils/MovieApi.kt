package app.beer.kinozver.utils

import app.beer.kinozver.models.auth.AuthResponse
import app.beer.kinozver.models.movie.MovieJSON
import app.beer.kinozver.models.movie.MovieValue
import app.beer.kinozver.models.section.SectionResponse
import app.beer.kinozver.models.user.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("auth")
    fun auth(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("user_social_id") userSocialId: String = "",
        @Query("name") name: String = "",
        @Query("photoUrl") photoUrl: String = ""
    ): Call<AuthResponse>

    @GET("get-user")
    fun getUserById(@Query("id") userId: Int): Call<UserResponse>

    @GET("sectioned-movies")
    fun getSectionedMovies(@Query("userId") userId: Int): Call<SectionResponse>

    @GET("home-app")
    fun getMoviesAll(@Query("page") page: Int): Call<MovieValue>

    @GET("json")
    fun getMoviesByType(@Query("type") type: String, @Query("page") page: Int): Call<MovieValue>

    @GET("json")
    fun getMovieByCategory(
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("cat") cat: String
    ): Call<MovieValue>

    @GET("get-movies-by-category")
    fun getMovieByCategory(@Query("genre") cat: String): Call<MovieValue>

    @GET("search")
    fun getMoviesBySearch(@Query("title") title: String): Call<MovieValue>

    @GET("get-movie-by-id")
    fun getMovieById(@Query("id") id: Int, @Query("userId") userId: Int): Call<MovieJSON>

    @GET("add-like-movie")
    fun addLikeOnMovie(@Query("movie_id") movieId: Int): Call<MovieValue>

    @GET("add-dislike-movie")
    fun addDislikeOnMovie(@Query("movie_id") movieId: Int): Call<MovieValue>

    @GET("minus-like-movie")
    fun minusLikeOnMovie(@Query("movie_id") movieId: Int): Call<MovieValue>

    @GET("minus-dislike-movie")
    fun minusDislikeOnMovie(@Query("movie_id") movieId: Int): Call<MovieValue>

}