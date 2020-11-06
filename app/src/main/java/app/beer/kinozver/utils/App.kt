package app.beer.kinozver.utils

import android.app.Application
import app.beer.kinozver.BuildConfig
import app.beer.kinozver.models.movie.MovieIteractor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://bazon.cc/api/"
const val BASE_MAPS_URL = "https://bazon.cc/api/"
const val API_KEY = "cb5e96c6aa1bd52944e49185639fdda6"

class App : Application() {

    private lateinit var movieApi: MovieApi
    private lateinit var movieIteractor: MovieIteractor

    fun getMovieApi(): MovieApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val url =chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("token", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@addInterceptor chain.proceed(request)
            }
            .build()

        movieApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
        return movieApi
    }

    fun getPlacesApi() {

    }

    fun getMovieIteractor(): MovieIteractor {
        movieIteractor = MovieIteractor()
        return movieIteractor
    }

}