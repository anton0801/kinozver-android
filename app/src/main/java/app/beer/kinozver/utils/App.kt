package app.beer.kinozver.utils

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://videocdn.tv/api/"
const val API_KEY = "ht74hfMgvYHQRZi2qTOfae4BIQSxkK7n"
const val LIMIT = 30

class App : Application() {

    private lateinit var retrofit: Retrofit
    private lateinit var movieApi: MovieApi

    override fun onCreate() {
        super.onCreate()

        val interceptor = HttpLoggingInterceptor()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val request = Request.Builder()
                    .addHeader("limit", "$LIMIT")
                    .addHeader("pretty_print", "1")
                    .addHeader("api_token", API_KEY)
                    .build()
                return@addInterceptor chain.proceed(request)
            }
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieApi = retrofit.create(MovieApi::class.java)
    }

    fun getMovieApi(): MovieApi {
        return movieApi
    }

}