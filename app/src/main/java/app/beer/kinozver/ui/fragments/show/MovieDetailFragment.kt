package app.beer.kinozver.ui.fragments.show

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import app.beer.kinozver.R
import app.beer.kinozver.models.Movie
import app.beer.kinozver.utils.replaceFragment
import kotlinx.android.synthetic.main.fragment_movie_detail.*

class MovieDetailFragment : Fragment() {

    lateinit var movie: Movie

    lateinit var btnShowMovie: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onStart() {
        super.onStart()

        movie = arguments?.getSerializable(EXTRA_MOVIE) as Movie
        initFields()
    }

    private fun initFields() {
        btnShowMovie = btn_show_movie
        btnShowMovie.setOnClickListener {
            replaceFragment(ShowMovieFragment.newInstance(movie.id))
        }
    }

    companion object {
        const val EXTRA_MOVIE = "EXTRA_MOVIE"

        fun newInstance(movie: Movie): Fragment {
            val args = Bundle()
            args.putSerializable(EXTRA_MOVIE, movie)
            val fragment = MovieDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}