package app.beer.kinozver.ui.fragments.show

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.BindingMethod
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import app.beer.kinozver.R
import app.beer.kinozver.databinding.FragmentMovieDetailBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import app.beer.kinozver.models.movie.MovieJSON
import app.beer.kinozver.models.movie.MovieValue
import app.beer.kinozver.utils.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.movie_item.*

class MovieDetailFragment : Fragment() {

    var movieId: Int? = 0

    lateinit var movie: MovieJSON
    lateinit var btnShowMovie: Button
    lateinit var binding: FragmentMovieDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        init()
        getMovie()
    }

    private fun init() {
        movieId = arguments?.getInt(EXTRA_MOVIE_ID)
        btnShowMovie = btn_show_movie
        btnShowMovie.setOnClickListener {
            replaceFragment(ShowMovieFragment.newInstance(movieId!!), true)
        }

        APP_ACTIVITY.viewModel.getError().observe(APP_ACTIVITY, Observer {
            if (it == "error 404") {
                not_found_error.visibility = View.VISIBLE
            } else {
                not_found_error.visibility = View.GONE
            }
        })
    }

    private fun initFields() {
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        APP_ACTIVITY.toolbar.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
        APP_ACTIVITY.toolbar.title = movie.info.title
        APP_ACTIVITY.bottomNavigationView.visibility = View.VISIBLE

        // movie_image.downloadAndSetImage(movie.info.posterUrl)
        binding.movie = movie
        val quality = movie.maxQual
        movie_quality.text =
            if (quality == "2160") "ULTRA HD" else if (quality == "1080") "FULL HD" else "HD"
    }

    private fun initSimilaresRecyclerView() {
        val sm = getSimilaresMovies()
        val rv = similares_movies
        val a = BaseMovieAdapter(sm)
        rv.adapter = a
        rv.layoutManager = GridLayoutManager(APP_ACTIVITY, 2)
    }

    private fun getMovie() {
        movie_loader.visibility = View.VISIBLE
        if (movieId != null) {
            not_found_error.visibility = View.GONE
            APP_ACTIVITY.viewModel.getMovieById(movieId.toString()).observe(APP_ACTIVITY, Observer {
                movie = it
                initFields()
                initSimilaresRecyclerView()
                movie_loader.visibility = View.GONE
            })
        } else {
            not_found_error.visibility = View.VISIBLE
        }
    }

    private fun getSimilaresMovies(): ArrayList<MovieJSON> {
        val similarMovies: ArrayList<MovieJSON> = ArrayList()
        APP_ACTIVITY.viewModel.getMoviesByCategory(movie.info.genre)
            .observe(APP_ACTIVITY, Observer {
                similarMovies.addAll(it)
            })
        return similarMovies
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

//        @BindingConversion
//        fun convertListToString(movies: List<MovieJSON>): String {
//            return "Pets size is: ${movies.size}"
//        }

//        @BindingAdapter({"app:movieImageUrl"})
//        fun loadImage(imageView: ImageView, url: String) {
//            imageView.downloadAndSetImage(url)
//        }

    }

}