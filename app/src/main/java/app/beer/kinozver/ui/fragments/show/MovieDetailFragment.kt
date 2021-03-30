package app.beer.kinozver.ui.fragments.show

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beer.kinozver.R
import app.beer.kinozver.models.movie.MovieIteractor
import app.beer.kinozver.models.movie.MovieJSON
import app.beer.kinozver.utils.*
import kotlinx.android.synthetic.main.activity_main.*

var IS_MOVIE_LIKED_KEY = "is_movie_liked"
var IS_MOVIE_DISLIKED_KEY = "is_movie_disliked"

class MovieDetailFragment : Fragment() {

    private var movieId: Int? = 0

    private lateinit var movie: MovieJSON
    private lateinit var btnShowMovie: Button

    private var similaresMovie: ArrayList<MovieJSON> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BaseMovieAdapter

    private lateinit var movieImage: ImageView
    private lateinit var movieIsNew: TextView
    private lateinit var movieYear: TextView
    private lateinit var movieAge: TextView
    private lateinit var movieDescription: TextView
    private lateinit var movieActors: TextView
    private lateinit var movieDirector: TextView
    private lateinit var movieQuality: TextView
    private lateinit var movieDurationOrEpisodes: TextView
    private lateinit var movieDownloadBtn: ImageView

    private lateinit var notFoundError: ConstraintLayout
    private lateinit var movieLoader: ConstraintLayout

    private lateinit var movieAddLikeBtn: AppCompatButton
    private lateinit var movieAddDislikeBtn: Button
    private lateinit var movieShareBtn: Button

    private lateinit var sharedManager: SharedManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        APP_ACTIVITY.supportActionBar?.show()
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    // пример чтобы брать филм из storage
//    val path =
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) FileProvider.getUriForFile(
//            APP_ACTIVITY,
//            APP_ACTIVITY.packageName + ".provider",
//            file
//        ) else
//            Uri.fromFile(file)

    /**
    * TODO: Сделать так чтобы фильмы можно было скачивать и смотреть в offline
    * и эти фильмы выводить в аккаунте в пункте скачанные фильмы или на главной в меню вывести с заголовком скачанные фильмы
    */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        APP_ACTIVITY.bottomNavigationView.visibility = View.VISIBLE
        sharedManager = SharedManager()

        with(view) {
            movieImage = findViewById(R.id.movie_image)
            movieIsNew = findViewById(R.id.movie_is_new)
            movieYear = findViewById(R.id.movie_year)
            movieAge = findViewById(R.id.movie_age)
            movieDescription = findViewById(R.id.movie_description)
            movieActors = findViewById(R.id.movie_actors)
            movieDirector = findViewById(R.id.movie_director)
            movieQuality = findViewById(R.id.movie_quality)
            movieDurationOrEpisodes = findViewById(R.id.movie_duration_or_episodes)
            notFoundError = findViewById(R.id.not_found_error)
            movieLoader = findViewById(R.id.movie_loader)
            btnShowMovie = findViewById(R.id.btn_show_movie)
            movieDownloadBtn = findViewById(R.id.movie_download_btn)

            recyclerView = findViewById(R.id.similares_movies)

            movieAddLikeBtn = findViewById(R.id.movie_add_like_btn)
            movieAddDislikeBtn = findViewById(R.id.movie_add_dislike_btn)
            movieShareBtn = findViewById(R.id.movie_share_btn)
        }

        adapter = BaseMovieAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(APP_ACTIVITY, 2)
    }

    override fun onStart() {
        super.onStart()
        movieLoader.visibility = View.VISIBLE
        init()
    }

    private fun init() {
        movieId = arguments?.getInt(EXTRA_MOVIE_ID)

        APP_ACTIVITY.viewModel.getError().observe(APP_ACTIVITY, Observer {
            if (it == "error 404") {
                notFoundError.visibility = View.VISIBLE
            } else {
                notFoundError.visibility = View.GONE
            }
        })

        getMovie()
    }

    private fun initFields() {
        IS_MOVIE_LIKED_KEY += "_$movieId"
        IS_MOVIE_DISLIKED_KEY += "_$movieId"

        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY.toolbar.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
        APP_ACTIVITY.toolbar.title = movie.title
        APP_ACTIVITY.bottomNavigationView.visibility = View.VISIBLE
        btnShowMovie.setOnClickListener {
            if (movie.continueEpisode != 0 && movie.continueSeria != 0) {
                replaceFragment(
                    ShowMovieFragment.newInstance(
                        movie.idM.toInt(),
                        movie.continueEpisode,
                        movie.continueSeria,
                        movie.continueWatchTime
                    ), true
                )
            } else if (movie.continueWatchTime != 0) {
                replaceFragment(ShowMovieFragment.newInstance(movie.idM.toInt(), time = movie.continueWatchTime), true)
            } else {
                replaceFragment(ShowMovieFragment.newInstance(movie.idM.toInt()), true)
            }
        }

        movieImage.downloadAndSetImage(movie.posterUrl)
        movieIsNew.visibility = if (movie.isNew) View.VISIBLE else View.GONE
        movieYear.text = movie.year
        movieAge.text = "${movie.age}+"
        movieDescription.text = movie.description
        movieActors.text = movie.actors
        movieDirector.text = movie.director
        val quality = movie.maxQual
        movieQuality.text =
            if (quality == "2160") "ULTRA HD" else if (quality == "1080") "FULL HD" else "HD"
        movieDurationOrEpisodes.text =
            if (movie.isSerial == "1") "5 сезонов" else "${movie.duration.toInt() / 60} минут"

        if (sharedManager.getBoolean(IS_MOVIE_LIKED_KEY)) {
            movieAddLikeBtn.compoundDrawableTintList =
                ColorStateList.valueOf(APP_ACTIVITY.getColor(android.R.color.holo_blue_light))
        } else {
            movieAddLikeBtn.compoundDrawableTintList =
                ColorStateList.valueOf(APP_ACTIVITY.getColor(android.R.color.darker_gray))
        }

        if (sharedManager.getBoolean(IS_MOVIE_DISLIKED_KEY)) {
            movieAddDislikeBtn.compoundDrawableTintList =
                ColorStateList.valueOf(APP_ACTIVITY.getColor(android.R.color.holo_blue_light))
        } else {
            movieAddDislikeBtn.compoundDrawableTintList =
                ColorStateList.valueOf(APP_ACTIVITY.getColor(android.R.color.darker_gray))
        }

        movieAddLikeBtn.setOnClickListener {
            if (sharedManager.getBoolean(IS_MOVIE_LIKED_KEY)) {
                APP_ACTIVITY.viewModel.movieIteractor.minusMovieLike(
                    movie.idM.toInt(),
                    object : MovieIteractor.MovieActionIteractorCallback {
                        override fun onSuccess(message: String) {
                            showToast("Вы убрали лайк с фильма \"${movie.title}\"")
                            sharedManager.putBoolean(IS_MOVIE_LIKED_KEY, false)
                            sharedManager.putBoolean(IS_MOVIE_DISLIKED_KEY, false)

                            movieAddLikeBtn.compoundDrawableTintList =
                                ColorStateList.valueOf(APP_ACTIVITY.getColor(android.R.color.darker_gray))
                        }

                        override fun onError(error: String) {}
                    })
            } else {
                APP_ACTIVITY.viewModel.movieIteractor.addMovieLike(
                    movie.idM.toInt(),
                    object : MovieIteractor.MovieActionIteractorCallback {
                        override fun onSuccess(message: String) {
                            showToast(message)
                            sharedManager.putBoolean(IS_MOVIE_LIKED_KEY, true)
                            sharedManager.putBoolean(IS_MOVIE_DISLIKED_KEY, false)

                            movieAddLikeBtn.compoundDrawableTintList =
                                ColorStateList.valueOf(APP_ACTIVITY.getColor(android.R.color.holo_blue_light))
                        }

                        override fun onError(error: String) {}
                    })
            }
        }
        movieAddDislikeBtn.setOnClickListener {
            if (sharedManager.getBoolean(IS_MOVIE_DISLIKED_KEY)) {
                APP_ACTIVITY.viewModel.movieIteractor.minusMovieDislike(
                    movie.idM.toInt(),
                    object : MovieIteractor.MovieActionIteractorCallback {
                        override fun onSuccess(message: String) {
                            showToast("Вы убрали дизлайк с фильма \"${movie.title}\"")
                            sharedManager.putBoolean(IS_MOVIE_LIKED_KEY, false)
                            sharedManager.putBoolean(IS_MOVIE_DISLIKED_KEY, false)

                            movieAddDislikeBtn.compoundDrawableTintList =
                                ColorStateList.valueOf(APP_ACTIVITY.getColor(android.R.color.darker_gray))
                        }

                        override fun onError(error: String) {}
                    })
            } else {
                APP_ACTIVITY.viewModel.movieIteractor.addMovieDislike(
                    movie.idM.toInt(),
                    object : MovieIteractor.MovieActionIteractorCallback {
                        override fun onSuccess(message: String) {
                            showToast(message)
                            sharedManager.putBoolean(IS_MOVIE_LIKED_KEY, false)
                            sharedManager.putBoolean(IS_MOVIE_DISLIKED_KEY, true)

                            movieAddDislikeBtn.compoundDrawableTintList =
                                ColorStateList.valueOf(APP_ACTIVITY.getColor(android.R.color.holo_blue_light))
                        }

                        override fun onError(error: String) {}
                    })
            }
        }
        movieShareBtn.setOnClickListener {
            val intentShare = Intent(Intent.ACTION_SEND)
            intentShare.type = "text/plane"
            intentShare.putExtra(Intent.EXTRA_TEXT, "$BASE_URL/show/movie/${movie.kp_id}")
            startActivity(Intent.createChooser(intentShare, "Выберите приложение"))
        }

        movieDownloadBtn.setOnClickListener { downloadMovie() }
    }

    private fun downloadMovie() {
        // TODO: Скачивать сам фильм и если он скачался то добовляем в roomDatabase (скачивать через foregroundService)
        val downloadIntent = Intent(APP_ACTIVITY, DownloadService::class.java).apply {
            putExtra(DownloadService.MOVIE_NAME_EXTRA, movie.title)
            putExtra(DownloadService.MOVIE_IMAGE_EXTRA, movie.posterUrl)
            putExtra(DownloadService.MOVIE_PATH_VIDEO_EXTRA, "")
            action = DownloadService.DOWNLOAD_ACTION
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            APP_ACTIVITY.startForegroundService(downloadIntent)
        } else {
            APP_ACTIVITY.startService(downloadIntent)
        }
    }

    private fun getMovie() {
        if (movieId != null) {
            notFoundError.visibility = View.GONE
            movieLoader.visibility = View.VISIBLE
            APP_ACTIVITY.viewModel.getMovieById(movieId!!, 3)
                .observe(APP_ACTIVITY, Observer {
                    movie = it
                    initFields()
                    getSimilaresMovies()
                })
        } else {
            movieLoader.visibility = View.GONE
            notFoundError.visibility = View.VISIBLE
        }
    }

    private fun getSimilaresMovies() {
        val genre = if (movie.genre.contains(",")) movie.genre.split(",")[0] else movie.genre
//        APP_ACTIVITY.viewModel.movieIteractor.getMovieByCategory(genre, object : MovieIteractor.MovieIteractorCallback {
//            override fun onSuccess(movies: List<MovieJSON>) {
//                similaresMovie.clear()
//                similaresMovie.addAll(movies)
//                adapter.setMovies(similaresMovie)
//                movieLoader.visibility = View.GONE
//            }
//
//            override fun onError(error: String) {
//
//            }
//        })
        APP_ACTIVITY.viewModel.getMoviesByCategory(genre)
            .observe(APP_ACTIVITY, Observer {
                similaresMovie.clear()
                similaresMovie.addAll(it)
                adapter.setMovies(similaresMovie)
                movieLoader.visibility = View.GONE
            })
    }

    companion object {
        const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun newInstance(movieId: Int): Fragment {
            val args = Bundle()
            args.putInt(EXTRA_MOVIE_ID, movieId)
            val fragment = MovieDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

}