package app.beer.kinozver.utils

import android.app.Application
import app.beer.kinozver.BuildConfig
import app.beer.kinozver.models.movie.MovieIteractor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://0338551b6cad.ngrok.io"
const val BASE_URL_API = "$BASE_URL/api/"
const val BASE_MAPS_URL = "https://bazon.cc/api/"
const val API_KEY = "cb5e96c6aa1bd52944e49185639fdda6"

class App : Application() {

    private lateinit var movieApi: MovieApi
    private lateinit var movieIteractor: MovieIteractor

    override fun onCreate() {
        super.onCreate()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        movieApi = Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    fun getMovieApi(): MovieApi {
        return movieApi
    }

    /**
    * TODO: Сделать не только показ кинотеатров через places api но и как пройти до них через Directions Api
    */
    fun getPlacesApi() {

    }

    fun getMovieIteractor(): MovieIteractor {
        movieIteractor = MovieIteractor()
        return movieIteractor
    }

}